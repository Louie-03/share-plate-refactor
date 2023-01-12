package louie.hanse.shareplate.integration.share;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.EMPTY_SHARE_INFO;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.OUT_OF_SCOPE_FOR_KOREA;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("쉐어 추천 통합 테스트")
class ShareRecommendationIntegrationTest extends InitIntegrationTest {

    @Test
    void 회원_주변의_쉐어를_추천한다() {
        given(documentationSpec)
            .filter(document("share-recommendation-get"))
            .accept(APPLICATION_JSON_VALUE)
            .param("latitude", 36.6576769)
            .param("longitude", 127.026364)

            .when()
            .get("/shares/recommendation")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void longitude가_null값인_경우_예외를_발생시킨다() {
        given(documentationSpec)
            .filter(document("share-recommendation-get-failed-by-empty-share-info"))
            .accept(APPLICATION_JSON_VALUE)
            .param("longitude", 127.026364)

            .when()
            .get("/shares/recommendation")

            .then()
            .statusCode(EMPTY_SHARE_INFO.getStatusCode().value())
            .body("errorCode", equalTo(EMPTY_SHARE_INFO.getErrorCode()))
            .body("message", equalTo(EMPTY_SHARE_INFO.getMessage()));
    }

    @Test
    void 대한민국의_위도_범위를_벗어난_경우_예외를_발생시킨다() {
        given(documentationSpec)
            .filter(document("share-recommendation-get-failed-by-out-of-scope-for-korea"))
            .accept(APPLICATION_JSON_VALUE)
            .param("latitude", 39)
            .param("longitude", 127.026364)

            .when()
            .get("/shares/recommendation")

            .then()
            .statusCode(OUT_OF_SCOPE_FOR_KOREA.getStatusCode().value())
            .body("errorCode", equalTo(OUT_OF_SCOPE_FOR_KOREA.getErrorCode()))
            .body("message", equalTo(OUT_OF_SCOPE_FOR_KOREA.getMessage()));
    }

    @Test
    void 대한민국의_경도_범위를_벗어난_경우_예외를_발생시킨다() {
        given(documentationSpec)
            .filter(document("share-recommendation-get-failed-by-out-of-scope-for-korea"))
            .accept(APPLICATION_JSON_VALUE)
            .param("latitude", 36.6576769)
            .param("longitude", 123)

            .when()
            .get("/shares/recommendation")

            .then()
            .statusCode(OUT_OF_SCOPE_FOR_KOREA.getStatusCode().value())
            .body("errorCode", equalTo(OUT_OF_SCOPE_FOR_KOREA.getErrorCode()))
            .body("message", equalTo(OUT_OF_SCOPE_FOR_KOREA.getMessage()));
    }
}
