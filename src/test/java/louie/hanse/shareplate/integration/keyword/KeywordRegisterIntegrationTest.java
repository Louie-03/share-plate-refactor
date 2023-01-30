package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.common.exception.invalid.type.InvalidExceptionType.INVALID_KEYWORD_CONTENTS;
import static louie.hanse.shareplate.common.exception.invalid.type.InvalidExceptionType.INVALID_LATITUDE;
import static louie.hanse.shareplate.common.exception.invalid.type.InvalidExceptionType.INVALID_LOCATION;
import static louie.hanse.shareplate.common.exception.type.KeywordExceptionType.DUPLICATE_KEYWORD;
import static louie.hanse.shareplate.common.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.util.Map;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("키워드 등록 통합 테스트")
class KeywordRegisterIntegrationTest extends InitIntegrationTest {

    @Test
    void 새로_생긴_쉐어에_대한_알림을_받을_수_있도록_키워드를_등록한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        Map<String, Object> requestBody = Map.of(
            "location", "청담동",
            "latitude", 37.524159,
            "longitude", 126.872879,
            "contents", "떡볶이"
        );

        given(documentationSpec)
            .filter(document("keyword-register-post"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/keywords")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void keyword가_비어있는_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        Map<String, Object> requestBody = Map.of(
            "location", "목동",
            "latitude", 37.5370192,
            "longitude", 126.872879
        );

        given(documentationSpec)
            .filter(document("keyword-register-post-failed-by-empty-keyword-info"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/keywords")

            .then()
            .statusCode(INVALID_KEYWORD_CONTENTS.getStatusCode())
            .body("errorCode", equalTo(INVALID_KEYWORD_CONTENTS.getErrorCode()));
    }

    @Test
    void location이_비어있는_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        Map<String, Object> requestBody = Map.of(
            "contents", "햄버거",
            "latitude", 37.5370192,
            "longitude", 126.872879
        );

        given(documentationSpec)
            .filter(document("keyword-register-post-failed-by-empty-keyword-info"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/keywords")

            .then()
            .statusCode(INVALID_LOCATION.getStatusCode())
            .body("errorCode", equalTo(INVALID_LOCATION.getErrorCode()));
    }

    @Test
    void 유효하지_않은_회원일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        Map<String, Object> requestBody = Map.of(
            "location", "목동",
            "latitude", 37.524159,
            "longitude", 126.872879,
            "contents", "떡볶이"
        );

        given(documentationSpec)
            .filter(document("keyword-register-post-failed-by-member-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/keywords")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 중복_키워드일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, Object> requestBody = Map.of(
            "location", "목동",
            "latitude", 37.524159,
            "longitude", 126.872879,
            "contents", "떡볶이"
        );

        given(documentationSpec)
            .filter(document("keyword-register-post-failed-by-duplicate-keyword"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/keywords")

            .then()
            .statusCode(DUPLICATE_KEYWORD.getStatusCode().value())
            .body("errorCode", equalTo(DUPLICATE_KEYWORD.getErrorCode()))
            .body("message", equalTo(DUPLICATE_KEYWORD.getMessage()));
    }

    @Test
    void 대한민국_위도_경도를_벗어난_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        Map<String, Object> requestBody = Map.of(
            "location", "목동",
            "latitude", 30.524159,
            "longitude", 126.872879,
            "contents", "떡볶이"
        );

        given(documentationSpec)
            .filter(document("keyword-register-post-failed-by-out-of-scope-for-korea"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/keywords")

            .then()
            .statusCode(INVALID_LATITUDE.getStatusCode())
            .body("errorCode", equalTo(INVALID_LATITUDE.getErrorCode()));
    }

}
