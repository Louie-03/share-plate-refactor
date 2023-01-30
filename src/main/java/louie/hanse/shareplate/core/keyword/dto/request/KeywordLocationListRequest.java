package louie.hanse.shareplate.core.keyword.dto.request;

import lombok.Setter;
import louie.hanse.shareplate.common.domain.Location;

@Setter
public class KeywordLocationListRequest {

    private String location;

    public Location toLocation() {
        return new Location(location);
    }
}
