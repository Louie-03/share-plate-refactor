package louie.hanse.shareplate.core.keyword.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.keyword.service.KeywordService;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordListResponse;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordLocationListResponse;
import louie.hanse.shareplate.core.keyword.dto.request.KeywordRegisterRequest;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordRegisterResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/keywords")
@RestController
@Validated
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping
    public List<KeywordListResponse> getKeywords(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return keywordService.getKeywords(memberId);
    }

    @GetMapping("/location")
    public KeywordLocationListResponse getLocations(
        @NotBlank(message = "요청한 키워드정보 필드값이 비어있습니다.") @RequestParam(value = "location", required = false) String location,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return keywordService.getLocations(memberId, location);
    }

    @PostMapping
    public KeywordRegisterResponse register(
        @RequestBody @Valid KeywordRegisterRequest keywordRegisterRequest,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return keywordService.register(keywordRegisterRequest, memberId);
    }

    @DeleteMapping("/{id}")
    public void delete(
        @PathVariable(required = false) @NotNull(message = "PathVariable의 keywordId가 비어있습니다.") @Positive(message = "키워드 id는 양수여야 합니다.") Long id,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        keywordService.delete(id, memberId);
    }

    @DeleteMapping
    public void deleteAll(
        @RequestBody(required = false) @Valid Map<String, @Valid @NotNull(message = "요청한 키워드정보 필드값이 비어있습니다.") String> map,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        String location = map.get("location");
        keywordService.deleteAll(memberId, location);
    }
}
