package louie.hanse.shareplate.integration.entry.utils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import louie.hanse.shareplate.core.share.domain.ShareType;
import louie.hanse.shareplate.core.share.dto.request.ShareRegisterRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class EntryIntegrationTestUtils {

    public static ShareRegisterRequest createShareRegisterRequest(LocalDateTime closedDateTime) {
        List<MultipartFile> images = List.of(new MockMultipartFile(
            "이미지", "test.txt".getBytes(StandardCharsets.UTF_8)));
        List<String> hashtags = List.of("피자", "도미노피자");

        return new ShareRegisterRequest(
            ShareType.DELIVERY,
            images,
            "테스트를 위한 제목",
            3000,
            12000,
            3,
            "강남역",
            "코드스쿼드",
            true,
            false,
            hashtags,
            37.498095,
            127.027610,
            "도미노 피자에 대해서 어쩌구",
            closedDateTime
        );
    }
}
