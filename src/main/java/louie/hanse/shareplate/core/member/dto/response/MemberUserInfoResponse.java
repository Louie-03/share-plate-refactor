package louie.hanse.shareplate.core.member.dto.response;

import lombok.Getter;
import louie.hanse.shareplate.core.member.domain.Member;

@Getter
public class MemberUserInfoResponse {
    private String profileImageUrl;
    private String nickname;
    private String email;

    public MemberUserInfoResponse(Member member) {
        this.profileImageUrl = member.getProfileImageUrl();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
    }
}
