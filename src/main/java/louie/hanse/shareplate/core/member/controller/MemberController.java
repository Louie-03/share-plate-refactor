package louie.hanse.shareplate.core.member.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.member.service.MemberService;
import louie.hanse.shareplate.core.member.dto.request.MemberChangeUserInfoRequest;
import louie.hanse.shareplate.core.member.dto.response.MemberUserInfoResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
@Validated
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public MemberUserInfoResponse getUserInfo(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return memberService.getUserInfo(memberId);
    }

    @PatchMapping
    public void changeUserInfo(@Valid MemberChangeUserInfoRequest memberChangeUserInfoRequest,
        HttpServletRequest request) throws IOException {
        Long memberId = (Long) request.getAttribute("memberId");
        memberService.changeUserInfo(memberChangeUserInfoRequest, memberId);
    }

}
