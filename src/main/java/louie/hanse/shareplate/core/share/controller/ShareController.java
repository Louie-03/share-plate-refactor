package louie.hanse.shareplate.core.share.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.argumentresolver.MemberVerification;
import louie.hanse.shareplate.core.share.dto.request.ShareEditRequest;
import louie.hanse.shareplate.core.share.dto.request.ShareMineSearchRequest;
import louie.hanse.shareplate.core.share.dto.request.ShareRecommendationRequest;
import louie.hanse.shareplate.core.share.dto.request.ShareRegisterRequest;
import louie.hanse.shareplate.core.share.dto.request.ShareSearchRequest;
import louie.hanse.shareplate.core.share.dto.response.ShareDetailResponse;
import louie.hanse.shareplate.core.share.dto.response.ShareRecommendationResponse;
import louie.hanse.shareplate.core.share.dto.response.ShareSearchResponse;
import louie.hanse.shareplate.core.share.dto.response.ShareWriterResponse;
import louie.hanse.shareplate.core.share.service.ShareService;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/shares")
@RestController
@Validated
public class ShareController {

    private final ShareService shareService;

    @PostMapping
    public Map<String, Long> register(@Valid ShareRegisterRequest shareRegisterRequest,
        @MemberVerification Long memberId)
        throws IOException {

        Map<String, Long> map = shareService.register(shareRegisterRequest, memberId);

        return Collections.singletonMap("entryId", map.get("entryId"));
    }

    @GetMapping
    public List<ShareSearchResponse> searchAroundMember(
        @Valid ShareSearchRequest shareSearchRequest) {

        return shareService.searchAroundMember(shareSearchRequest);
    }

    @GetMapping("/mine")
    public List<ShareSearchResponse> searchMine(
        @Valid ShareMineSearchRequest shareMineSearchRequest, @MemberVerification Long memberId) {

        return shareService.searchMine(shareMineSearchRequest, memberId);
    }

    @GetMapping("/{id}")
    public ShareDetailResponse getDetail(@PathVariable(required = false)
    @NotNull(message = "PathVariable의 shareId가 비어있습니다.")
    @Positive(message = "쉐어 id는 양수여야 합니다.") Long id, HttpServletRequest request) {

        return shareService.getDetail(id, request.getHeader(HttpHeaders.AUTHORIZATION));
    }

    @PutMapping("/{id}")
    public void edit(
        @PathVariable(required = false) @NotNull(message = "PathVariable의 shareId가 비어있습니다.")
        @Positive(message = "쉐어 id는 양수여야 합니다.") Long id, @Valid ShareEditRequest shareEditRequest,
        @MemberVerification Long memberId) throws IOException {

        shareService.edit(shareEditRequest, id, memberId);
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable @Positive(message = "쉐어 id는 양수여야 합니다.") Long id,
        @MemberVerification Long memberId) {

        shareService.cancel(id, memberId);
    }

    @GetMapping("/recommendation")
    public List<ShareRecommendationResponse> recommendationAroundMember(
        @Valid ShareRecommendationRequest request) {

        return shareService.recommendationAroundMember(request);
    }

    @GetMapping("/writer")
    public ShareWriterResponse getWriteByMember(
        @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.") @RequestParam(required = false)
        Long writerId) {

        return shareService.getWriteByMember(writerId);
    }
}
