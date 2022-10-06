package louie.hanse.shareplate.integration.share;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.EMPTY_SHARE_INFO;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("쉐어 기능 통합 테스트")
class ShareWriterSearchIntegrationTest extends InitIntegrationTest {

    @Test
    void 특정_사용자가_작성한_쉐어를_조회한다() {
        given(documentationSpec)
            .param("writerId", 2370842997L)
            .filter(document("share-write-by-member-get"))

            .when()
            .get("/shares/writer")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("writer", equalTo("정현석"))
            .body("thumbnailUrl", containsString("http://"))
            .body("shareCount", equalTo(3))
            .body("shares", hasSize(3))
            .body("shares[0].id", equalTo(1))
            .body("shares[0].thumbnailUrl", containsString("https://"))
            .body("shares[0].title", equalTo("강남역에서 떡볶이 먹을 사람 모집합니다."))
            .body("shares[0].location", equalTo("강남역"))
            .body("shares[0].price", equalTo(10000))
            .body("shares[0].createdDateTime", equalTo("2022-08-03 16:00"))
            .body("shares[0].closedDateTime", equalTo("2023-08-03 16:00"));
    }

    @Test
    void 만약_writerId를_입력하지_않았다면_예외를_발생시킨다() {
        given(documentationSpec)
            .param("writerId", " ")
            .filter(document("share-write-by-member-get"))

            .when()
            .get("/shares/writer")

            .then()
            .statusCode(EMPTY_SHARE_INFO.getStatusCode().value())
            .body("errorCode", equalTo(EMPTY_SHARE_INFO.getErrorCode()))
            .body("message", equalTo(EMPTY_SHARE_INFO.getMessage()));
    }

    @Test
    void 존재하지_않는_회원에_대한_쉐어를_검색한다면_예외를_발생시킨다() {
        given(documentationSpec)
            .param("writerId", "12345")
            .filter(document("share-write-by-member-get"))

            .when()
            .get("/shares/writer")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }
}