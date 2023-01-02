package louie.hanse.shareplate.core.member.dto.request;

import lombok.Getter;
import lombok.Setter;
import louie.hanse.shareplate.common.validator.member.ValidMemberImage;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MemberChangeUserInfoRequest {

    @ValidMemberImage
    private MultipartFile image;

    private String nickname;
}
