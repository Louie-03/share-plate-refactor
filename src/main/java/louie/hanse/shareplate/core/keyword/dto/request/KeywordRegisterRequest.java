package louie.hanse.shareplate.core.keyword.dto.request;

import lombok.Getter;

@Getter
public class KeywordRegisterRequest {

    private String contents;
    private String location;
    private Double latitude;
    private Double longitude;

}
