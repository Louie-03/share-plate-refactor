package louie.hanse.shareplate.core.keyword.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.domain.Location;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.KeywordExceptionType;
import louie.hanse.shareplate.core.keyword.domain.Keyword;
import louie.hanse.shareplate.core.keyword.dto.request.KeywordLocationDeleteRequest;
import louie.hanse.shareplate.core.keyword.dto.request.KeywordLocationListRequest;
import louie.hanse.shareplate.core.keyword.dto.request.KeywordRegisterRequest;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordListResponse;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordLocationListResponse;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordRegisterResponse;
import louie.hanse.shareplate.core.keyword.repository.KeywordRepository;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.core.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final MemberService memberService;

    @Transactional
    public KeywordRegisterResponse register(KeywordRegisterRequest request, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Keyword keyword = request.toEntity(member);
        boolean existKeyword = keywordRepository.existsByMemberIdAndContentsAndLocation(memberId,
            request.getContents(), request.getLocation());
        if (existKeyword) {
            throw new GlobalException(KeywordExceptionType.DUPLICATE_KEYWORD);
        }

        keywordRepository.save(keyword);

        return new KeywordRegisterResponse(keyword);
    }

    @Transactional
    public void delete(Long id, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Keyword keyword = findWithMemberByIdOrElseThrow(id);
        keyword.isNotMemberThrowException(member);

        keywordRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll(KeywordLocationDeleteRequest request, Long memberId) {
        Location location = request.toLocation();
        memberService.findByIdOrElseThrow(memberId);
        boolean existKeyword = keywordRepository.existsByMemberIdAndLocation(memberId, location);
        if (!existKeyword) {
            throw new GlobalException(KeywordExceptionType.KEYWORD_NOT_FOUND);
        }

        keywordRepository.deleteAllByMemberIdAndLocation(memberId, location);
    }

    public List<KeywordListResponse> getKeywords(Long memberId) {
        memberService.findByIdOrElseThrow(memberId);

        return keywordRepository.getKeywords(memberId);
    }

    public KeywordLocationListResponse getLocations(KeywordLocationListRequest request,
        Long memberId) {
        memberService.findByIdOrElseThrow(memberId);

        List<Keyword> keywords = keywordRepository.findAllByMemberIdAndLocation(
            memberId, request.toLocation());

//        TODO : DTO로 해당 로직 옮기기
        if (keywords.isEmpty()) {
            return new KeywordLocationListResponse();
        }
        return new KeywordLocationListResponse(keywords);
    }

    private Keyword findWithMemberByIdOrElseThrow(Long id) {
        return keywordRepository.findWithMemberById(id).orElseThrow(
            () -> new GlobalException(KeywordExceptionType.KEYWORD_NOT_FOUND));
    }
}
