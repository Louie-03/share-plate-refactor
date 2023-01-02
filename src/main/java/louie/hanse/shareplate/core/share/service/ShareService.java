package louie.hanse.shareplate.core.share.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoom;
import louie.hanse.shareplate.core.entry.domain.Entry;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.core.member.service.MemberService;
import louie.hanse.shareplate.core.share.domain.Share;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.ShareExceptionType;
import louie.hanse.shareplate.common.jwt.JwtProvider;
import louie.hanse.shareplate.core.entry.repository.EntryRepository;
import louie.hanse.shareplate.core.share.repository.ShareRepository;
import louie.hanse.shareplate.core.wish.repository.WishRepository;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoomType;
import louie.hanse.shareplate.core.share.domain.MineType;
import louie.hanse.shareplate.core.share.domain.ShareType;
import louie.hanse.shareplate.common.uploader.FileUploader;
import louie.hanse.shareplate.core.share.dto.response.ShareDetailResponse;
import louie.hanse.shareplate.core.share.dto.request.ShareEditRequest;
import louie.hanse.shareplate.core.share.dto.request.ShareMineSearchRequest;
import louie.hanse.shareplate.core.share.dto.request.ShareRecommendationRequest;
import louie.hanse.shareplate.core.share.dto.response.ShareRecommendationResponse;
import louie.hanse.shareplate.core.share.dto.request.ShareRegisterRequest;
import louie.hanse.shareplate.core.share.dto.request.ShareSearchRequest;
import louie.hanse.shareplate.core.share.dto.response.ShareSearchResponse;
import louie.hanse.shareplate.core.share.dto.response.ShareWriterResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ShareService {

    private final MemberService memberService;
    private final ShareRepository shareRepository;
    private final WishRepository wishRepository;
    private final EntryRepository entryRepository;
    private final JwtProvider jwtProvider;
    private final FileUploader fileUploader;

    @Transactional
    public Map<String, Long> register(ShareRegisterRequest request, Long memberId) throws IOException {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = request.toEntity(member);
        for (MultipartFile image : request.getImages()) {
            String uploadedImageUrl = fileUploader.uploadShareImage(image);
            share.addShareImage(uploadedImageUrl);
        }

        if (!ObjectUtils.isEmpty(request.getHashtags())) {
            for (String contents : request.getHashtags()) {
                share.addHashtag(contents);
            }
        }

        Entry entry = new Entry(share, member);
        entryRepository.save(entry);
        new ChatRoom(member, share, ChatRoomType.ENTRY);
        shareRepository.save(share);

        return Map.of("id", share.getId(), "entryId", entry.getId());
    }

    public List<ShareSearchResponse> searchAroundMember(
        ShareSearchRequest shareSearchRequest) {
        List<Share> shares = shareRepository.searchAroundMember(shareSearchRequest);
        return shares.stream()
            .map(ShareSearchResponse::new)
            .collect(Collectors.toList());
    }

    public List<ShareSearchResponse> searchMine(
        ShareMineSearchRequest shareMineSearchRequest, Long memberId) {
        ShareType type = shareMineSearchRequest.getShareType();
        boolean expired = shareMineSearchRequest.isExpired();
        LocalDateTime currentDateTime = LocalDateTime.now();

        Map<MineType, Supplier<List<Share>>> shareFindMapByMineType = Map.of(
            MineType.ENTRY, () -> shareRepository.findWithEntryByMemberIdAndTypeAndNotWriteByMeAndIsExpired(
                memberId, type, expired, currentDateTime),
            MineType.WRITER, () -> shareRepository.findByWriterIdAndTypeAndIsExpired(
                memberId, type, expired, currentDateTime),
            MineType.WISH, () -> shareRepository.findWithWishByMemberIdAndTypeAndIsExpired(
                memberId, type, expired, currentDateTime)
        );

        List<Share> shares = shareFindMapByMineType.get(shareMineSearchRequest.getMineType()).get();
        return shares.stream()
            .map(ShareSearchResponse::new)
            .collect(Collectors.toList());
    }

    public ShareDetailResponse getDetail(Long id, String accessToken) {
        Share share = findWithWriterByIdOrElseThrow(id);
        share.isCanceledThrowException();

        boolean check = true;
        Long memberId = null;
        try {
            if (StringUtils.hasText(accessToken)) {
                jwtProvider.verifyAccessToken(accessToken);
                memberId = jwtProvider.decodeMemberId(accessToken);
            } else {
                check = false;
            }
        } catch (JWTVerificationException e) {
            check = false;
        }

        boolean wish = false;
        boolean entry = false;
        Member member = null;
        if (check) {
            member = memberService.findByIdOrElseThrow(memberId);
            wish = wishRepository.existsByMemberIdAndShareId(memberId, id);
            entry = entryRepository.existsByMemberIdAndShareId(memberId, id);
        }

        if (check && !entry) {
            entry = shareRepository.existsByIdAndWriterId(id, memberId);
        }
        return new ShareDetailResponse(share, member, wish, entry);
    }

    @Transactional
    public void edit(ShareEditRequest request, Long id, Long memberId) throws IOException {
        Share findShare = findWithWriterByIdOrElseThrow(id);
        Member member = memberService.findByIdOrElseThrow(memberId);
        findShare.isNotWriterThrowException(member);
        findShare.isClosedThrowException();
        findShare.isCanceledThrowException();
        if (findShare.isLeftLessThanAnHour()) {
            throw new GlobalException(ShareExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_EDIT);
        }

        Share share = request.toEntity(id, member);
        for (MultipartFile image : request.getImages()) {
            String uploadImageUrl = fileUploader.uploadShareImage(image);
            share.addShareImage(uploadImageUrl);
        }

        if (!ObjectUtils.isEmpty(request.getHashtags())) {
            for (String contents : request.getHashtags()) {
                share.addHashtag(contents);
            }
        }
        shareRepository.save(share);
    }

    @Transactional
    public void cancel(Long id, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = findWithWriterByIdOrElseThrow(id);
        share.isNotWriterThrowException(member);
        if (share.isLeftLessThanAnHour()) {
            throw new GlobalException(ShareExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_CANCEL);
        }
        share.cancel();
    }

    public Share findByIdOrElseThrow(Long id) {
        return shareRepository.findById(id)
            .orElseThrow(() -> new GlobalException(ShareExceptionType.SHARE_NOT_FOUND));
    }

    public List<ShareRecommendationResponse> recommendationAroundMember(ShareRecommendationRequest request) {
        List<ShareRecommendationResponse> shareRecommendationResponses = shareRepository
            .recommendationAroundMember(request);
        Collections.shuffle(shareRecommendationResponses);
        return shareRecommendationResponses;
    }

    public ShareWriterResponse getWriteByMember(Long writerId) {
        Member writer = memberService.findByIdOrElseThrow(writerId);
        return new ShareWriterResponse(writer);
    }

    public Share findWithShareImageByIdOrElseThrow(Long id) {
        return shareRepository.findWithShareImageByIdOrElseThrow(id)
            .orElseThrow(() -> new GlobalException(ShareExceptionType.SHARE_NOT_FOUND));
    }

    public Share findWithWriterByIdOrElseThrow(Long id) {
        return shareRepository.findWithWriterById(id)
            .orElseThrow(() -> new GlobalException(ShareExceptionType.SHARE_NOT_FOUND));
    }

}
