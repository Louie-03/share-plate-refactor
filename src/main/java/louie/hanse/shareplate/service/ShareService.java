package louie.hanse.shareplate.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.ShareExceptionType;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.repository.EntryRepository;
import louie.hanse.shareplate.repository.ShareRepository;
import louie.hanse.shareplate.repository.WishRepository;
import louie.hanse.shareplate.type.MineType;
import louie.hanse.shareplate.web.dto.share.ShareDetailResponse;
import louie.hanse.shareplate.web.dto.share.ShareEditRequest;
import louie.hanse.shareplate.web.dto.share.ShareMineSearchRequest;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ShareService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${file.upload.location}")
    private String fileUploadLocation;

    private final MemberService memberService;
    private final ShareRepository shareRepository;
    private final WishRepository wishRepository;
    private final EntryRepository entryRepository;
    private final AmazonS3 amazonS3Client;
    private final JwtProvider jwtProvider;

    @Transactional
    public void register(ShareRegisterRequest request, Long memberId) throws IOException {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = request.toEntity(member);
        for (MultipartFile image : request.getImages()) {
            String uploadedImageUrl = uploadImage(image);
            share.addShareImage(uploadedImageUrl);
        }
        shareRepository.save(share);
    }

    public List<ShareSearchResponse> searchAroundMember(
        ShareSearchRequest shareSearchRequest, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        List<Share> shares = shareRepository.searchAroundMember(member, shareSearchRequest);
        return shares.stream()
            .map(ShareSearchResponse::new)
            .collect(Collectors.toList());
    }

    public List<ShareSearchResponse> searchMine(
        ShareMineSearchRequest shareMineSearchRequest, Long memberId) {
        MineType mineType = shareMineSearchRequest.getMineType();
        List<Share> shares = null;
        if (mineType.isEntry()) {
            shares = shareRepository.findWithEntry(memberId);
        }
        if (mineType.isWriter()) {
            shares = shareRepository.findByWriterId(memberId);
        }
        if (mineType.isWish()) {
            shares = shareRepository.findWithWish(memberId);
        }
        return shares.stream()
            .map(ShareSearchResponse::new)
            .collect(Collectors.toList());
    }

    private String uploadImage(MultipartFile image) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(image.getSize());

        String originalImageName = image.getOriginalFilename();
        String ext = originalImageName.substring(originalImageName.lastIndexOf(".") + 1);

        String storeFileName = UUID.randomUUID() + "." + ext;
        String key = fileUploadLocation + "/" + storeFileName;

        try (InputStream inputStream = image.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        }

        return amazonS3Client.getUrl(bucket, key).toString();
    }

    public ShareDetailResponse getDetail(Long id, String accessToken) {
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
        if (check) {
            wish = wishRepository.existsByMemberIdAndShareId(memberId, id);
            entry = entryRepository.existsByMemberIdAndShareId(memberId, id);
        }

        Share share = findByIdOrElseThrow(id);

        if (check && !entry) {
            entry = shareRepository.existsByIdAndWriterId(id, memberId);
        }
        return new ShareDetailResponse(share, wish, entry);
    }

    @Transactional
    public void edit(ShareEditRequest shareEditRequest, Long id, Long memberId) throws IOException {
        isNotWriterThrowException(id, memberId);
        Member writer = memberService.findByIdOrElseThrow(memberId);
        Share share = shareEditRequest.toEntity(id, writer);
        for (MultipartFile image : shareEditRequest.getImages()) {
            String uploadImageUrl = uploadImage(image);
            share.addShareImage(uploadImageUrl);
        }
        shareRepository.save(share);
    }

    @Transactional
    public void delete(Long id, Long memberId) {
        isNotWriterThrowException(id, memberId);
        Share share = findByIdOrElseThrow(id);
        shareRepository.delete(share);
    }

    private void isNotWriterThrowException(Long id, Long memberId) {
        if (isNotWriter(id, memberId)) {
            throw new GlobalException(ShareExceptionType.IS_NOT_WRITER);
        }
    }

    public Share findByIdOrElseThrow(Long id) {
        return shareRepository.findById(id)
            .orElseThrow(() -> new GlobalException(ShareExceptionType.SHARE_NOT_FOUND));
    }

    private boolean isNotWriter(Long id, Long memberId) {
        return !shareRepository.existsByIdAndWriterId(id, memberId);
    }
}