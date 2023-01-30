package louie.hanse.shareplate.core.keyword.dto.request;

import lombok.Getter;
import louie.hanse.shareplate.common.domain.Location;

@Getter
public class KeywordLocationDeleteRequest {

    private String location;

    public Location toLocation() {
        return new Location(location);
    }
}
