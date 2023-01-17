package louie.hanse.shareplate.core.keyword.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.domain.Latitude;
import louie.hanse.shareplate.common.domain.Location;
import louie.hanse.shareplate.common.domain.Longitude;
import louie.hanse.shareplate.core.keyword.domain.KeywordContents;
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
        @RequestParam(value = "location", required = false) String location,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return keywordService.getLocations(memberId, new Location(location));
    }

    @PostMapping
    public KeywordRegisterResponse register(
        @RequestBody KeywordRegisterRequest keywordRegisterRequest,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return keywordService.register(
            memberId,
            new KeywordContents(keywordRegisterRequest.getContents()),
            new Location(keywordRegisterRequest.getLocation()),
            new Latitude(keywordRegisterRequest.getLatitude()),
            new Longitude(keywordRegisterRequest.getLongitude())
        );
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
        @RequestBody(required = false) Map<String, String> map,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        Location location = new Location(map.get("location"));
        keywordService.deleteAll(memberId, location);
    }
}
