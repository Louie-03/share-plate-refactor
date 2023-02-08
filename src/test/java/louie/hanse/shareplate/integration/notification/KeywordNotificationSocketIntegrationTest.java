package louie.hanse.shareplate.integration.notification;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.MULTIPART;
import static louie.hanse.shareplate.integration.share.utils.ShareIntegrationTestUtils.createMultiPartSpecification;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import louie.hanse.shareplate.integration.InitSocketIntegrationTest;
import louie.hanse.shareplate.core.notification.dto.response.KeywordNotificationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("키워드 알림 통합 테스트")
class KeywordNotificationSocketIntegrationTest extends InitSocketIntegrationTest {

//    TODO : shareId 검증하도록 구현
    @Test
    void 키워드에_등록한_내용이_포함된_쉐어가_등록된_경우_실시간으로_키워드_알림을_전송한다()
        throws ExecutionException, InterruptedException, TimeoutException {
        Long keywordId = 10L;
        String destination = "/queue/notifications/keywords/" + keywordId;
        String subscribeAccessToken = jwtProvider.createAccessToken(2355841047L);

        stompSession.subscribe(createStompHeaders(subscribeAccessToken, destination),
            getStompSessionHandlerAdapter(KeywordNotificationResponse.class));

        String accessToken = jwtProvider.createAccessToken(2370842997L);
        String title = "키워드 알림 테스트용 제목";
        String location = "강남역";

        given(documentationSpec)
            .header(AUTHORIZATION, accessToken)
            .contentType(MULTIPART)
            .multiPart("images", "test1.jpg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test1.jpeg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.png", "def".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", title))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그1"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그2"))
            .multiPart(createMultiPartSpecification("locationGuide", "강남역 파출소 앞"))
            .multiPart(createMultiPartSpecification("location", location))
            .multiPart(createMultiPartSpecification("description", "설명"))
            .formParam("type", "delivery")
            .formParam("price", 10000)
            .formParam("originalPrice", 30000)
            .formParam("recruitment", 3)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", true)
            .formParam("latitude", 37.499237)
            .formParam("longitude", 127.026364)
            .formParam("closedDateTime", "2023-12-30 14:00")

            .when()
            .post("/shares")

            .then()
            .statusCode(HttpStatus.OK.value());

        KeywordNotificationResponse result = (KeywordNotificationResponse) completableFuture
            .get(3, TimeUnit.SECONDS);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getShareTitle()).isEqualTo(title);
        assertThat(result.getShareLocation()).isEqualTo(location);
        assertThat(result.getShareThumbnailImageUrl()).contains("http");
        assertThat(result.getNotificationCreatedDateTime()).isBefore(LocalDateTime.now());
    }
}
