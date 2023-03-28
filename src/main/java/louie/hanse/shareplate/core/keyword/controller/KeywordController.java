package louie.hanse.shareplate.core.keyword.controller;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.argumentresolver.MemberVerification;
import louie.hanse.shareplate.core.keyword.dto.request.KeywordLocationDeleteRequest;
import louie.hanse.shareplate.core.keyword.dto.request.KeywordLocationListRequest;
import louie.hanse.shareplate.core.keyword.dto.request.KeywordRegisterRequest;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordListResponse;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordLocationListResponse;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordRegisterResponse;
import louie.hanse.shareplate.core.keyword.service.KeywordService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/keywords")
@RestController
@Validated
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping
    public List<KeywordListResponse> getKeywords(@MemberVerification Long memberId) {
        return keywordService.getKeywords(memberId);
    }

    @GetMapping("/location")
    public KeywordLocationListResponse getLocations(
        KeywordLocationListRequest keywordLocationListRequest, @MemberVerification Long memberId) {
        return keywordService.getLocations(keywordLocationListRequest, memberId);
    }

    @PostMapping
    public KeywordRegisterResponse register(
        @RequestBody KeywordRegisterRequest keywordRegisterRequest, @MemberVerification Long memberId) {
        return keywordService.register(keywordRegisterRequest, memberId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(required = false)
    @NotNull(message = "PathVariable의 keywordId가 비어있습니다.")
    @Positive(message = "키워드 id는 양수여야 합니다.") Long id, @MemberVerification Long memberId) {
        keywordService.delete(id, memberId);
    }

    @DeleteMapping
    public void deleteAll(@RequestBody KeywordLocationDeleteRequest keywordLocationDeleteRequest,
        @MemberVerification Long memberId) {
        keywordService.deleteAll(keywordLocationDeleteRequest, memberId);
    }
}
