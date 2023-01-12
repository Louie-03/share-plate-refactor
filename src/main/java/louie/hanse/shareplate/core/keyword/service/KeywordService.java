package louie.hanse.shareplate.core.keyword.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.keyword.domain.Keyword;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.KeywordExceptionType;
import louie.hanse.shareplate.core.member.service.MemberService;
import louie.hanse.shareplate.core.keyword.repository.KeywordRepository;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordListResponse;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordLocationListResponse;
import louie.hanse.shareplate.core.keyword.dto.request.KeywordRegisterRequest;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordRegisterResponse;
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
        boolean existKeyword = keywordRepository.existsByMemberIdAndContentsAndLocation(memberId,
            request.getContents(), request.getLocation());
        if (existKeyword) {
            throw new GlobalException(KeywordExceptionType.DUPLICATE_KEYWORD);
        }
        Keyword keyword = request.toEntity(member);
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
    public void deleteAll(Long memberId, String location) {
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

    public KeywordLocationListResponse getLocations(Long memberId, String location) {
        memberService.findByIdOrElseThrow(memberId);
        List<Keyword> keywords = findAllByMemberIdAndLocation(memberId, location);

        if (keywords.isEmpty()) {
            return new KeywordLocationListResponse();
        }
        return new KeywordLocationListResponse(keywords);
    }

    private List<Keyword> findAllByMemberIdAndLocation(Long memberId, String location) {
        return keywordRepository.findAllByMemberIdAndLocation(
            memberId, location);
    }

    private Keyword findWithMemberByIdOrElseThrow(Long id) {
        return keywordRepository.findWithMemberById(id).orElseThrow(
            () -> new GlobalException(KeywordExceptionType.KEYWORD_NOT_FOUND));
    }
}
