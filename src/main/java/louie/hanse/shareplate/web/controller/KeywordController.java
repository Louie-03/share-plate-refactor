package louie.hanse.shareplate.web.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.KeywordService;
import louie.hanse.shareplate.web.dto.keyword.KeywordRegisterRequest;
import louie.hanse.shareplate.web.dto.keyword.KeywordRegisterResponse;
import louie.hanse.shareplate.web.dto.keyword.KeywordResponse;
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
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping
    public List<KeywordResponse> getKeywords(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return keywordService.getKeywords(memberId);
    }

    @PostMapping
    public KeywordRegisterResponse register(@RequestBody KeywordRegisterRequest keywordRegisterRequest,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return keywordService.register(keywordRegisterRequest, memberId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        keywordService.delete(id);
    }
}