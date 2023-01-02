package louie.hanse.shareplate.core.member.dto.request;

import lombok.Getter;

@Getter
public class MemberChangeLocationRequest {

    private String location;
    private double longitude;
    private double latitude;
}
