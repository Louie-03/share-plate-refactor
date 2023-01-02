package louie.hanse.shareplate.core.member.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.member.repository.MemberRepository;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.oauth.OAuthUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Transactional
    public Member login(OAuthUserInfo oauthUserInfo) {
        Member member = memberRepository.findById(oauthUserInfo.getId()).orElse(null);

        if (member == null) {
            member = oauthUserInfo.toMember();
            memberRepository.save(member);
        }
        return member;
    }

    @Transactional
    public void updateRefreshToken(String refreshToken, Long id) {
        Member member = memberService.findByIdOrElseThrow(id);
        member.changeRefreshToken(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(Long id) {
        Member member = memberService.findByIdOrElseThrow(id);
        member.deleteRefreshToken();
    }

    public String findRefreshTokenByMemberId(Long id) {
        return memberService.findByIdOrElseThrow(id).getRefreshToken();
    }
}
