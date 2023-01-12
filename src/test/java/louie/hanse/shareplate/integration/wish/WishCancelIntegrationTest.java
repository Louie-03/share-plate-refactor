package louie.hanse.shareplate.integration.wish;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.common.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.SHARE_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.SHARE_IS_CANCELED;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.SHARE_NOT_FOUND;
import static louie.hanse.shareplate.common.exception.type.WishExceptionType.EMPTY_WISH_INFO;
import static louie.hanse.shareplate.common.exception.type.WishExceptionType.SHARE_NOT_WISH;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.util.Collections;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("위시 취소 통합 테스트")
class WishCancelIntegrationTest extends InitIntegrationTest {

    @Test
    void 회원이_쉐어의_위시를_취소를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("wish-cancel-delete"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", 3))

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 쉐어_id가_null값인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("wish-cancel-delete-failed-by-share-id-null"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", null))

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(EMPTY_WISH_INFO.getStatusCode().value())
            .body("errorCode", equalTo(EMPTY_WISH_INFO.getErrorCode()))
            .body("message", equalTo(EMPTY_WISH_INFO.getMessage()));
    }

    @Test
    void 쉐어_id가_양수가_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("wish-cancel-delete-failed-by-share-id-not-positive"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", -3))

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(SHARE_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("wish-cancel-delete-failed-by-member-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", 3))

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 유효하지_않은_쉐어인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("wish-cancel-delete-failed-by-share-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", 22222))

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(SHARE_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(SHARE_NOT_FOUND.getMessage()));
    }

    @Test
    void 유효하지_않은_위시인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("wish-cancel-delete-failed-by-wish-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", 4))

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(SHARE_NOT_WISH.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_NOT_WISH.getErrorCode()))
            .body("message", equalTo(SHARE_NOT_WISH.getMessage()));
    }

    @Test
    void 취소된_쉐어인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("wish-cancel-delete-failed-by-already-share-is-canceled"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", 6))

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(SHARE_IS_CANCELED.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_IS_CANCELED.getErrorCode()))
            .body("message", equalTo(SHARE_IS_CANCELED.getMessage()));
    }


}
