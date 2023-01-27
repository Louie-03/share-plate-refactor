package louie.hanse.shareplate.core.keyword.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.core.keyword.domain.Keyword;

@Getter
@NoArgsConstructor
public class KeywordLocationListResponse {

    private Double longitude;
    private Double latitude;
    private List<KeywordDetailResponse> keywords = new ArrayList<>();

    public KeywordLocationListResponse(List<Keyword> keywords) {
        this.longitude = keywords.get(0).getLongitude();
        this.latitude = keywords.get(0).getLatitude();
        this.keywords = keywords.stream()
            .map(KeywordDetailResponse::new)
            .collect(Collectors.toList());
    }
}
