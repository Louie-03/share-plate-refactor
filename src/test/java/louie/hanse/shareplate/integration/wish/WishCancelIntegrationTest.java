package louie.hanse.shareplate.integration.wish;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.exception.type.ShareExceptionType;
import louie.hanse.shareplate.exception.type.WishExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("위시 취소 기능 통합 테스트")
class WishCancelIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 회원이_쉐어의_위시를_취소를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        JSONObject requestParam = new JSONObject();
        requestParam.put("shareId", 3);

        given(documentationSpec)
            .filter(document("wish-request-cancel-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestParam)

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원이_쉐어_id_값을_빈값으로_찜_취소를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        JSONObject requestParam = new JSONObject();
        requestParam.put("shareId", null);

        given(documentationSpec)
            .filter(document("wish-request-cancel-empty-of-share-id"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestParam)

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(WishExceptionType.EMPTY_WISH_INFO.getStatusCode().value())
            .body("errorCode", equalTo(WishExceptionType.EMPTY_WISH_INFO.getErrorCode()))
            .body("message", equalTo(WishExceptionType.EMPTY_WISH_INFO.getMessage()));
    }

    @Test
    void 회원이_쉐어_id_값을_음수로_찜_취소를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        JSONObject requestParam = new JSONObject();
        requestParam.put("shareId", -3);

        given(documentationSpec)
                .filter(document("wish-request-cancel-negative-of-share-id"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestParam)

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(ShareExceptionType.SHARE_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(ShareExceptionType.SHARE_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(ShareExceptionType.SHARE_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원이_쉐어의_위시를_취소를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        JSONObject requestParam = new JSONObject();
        requestParam.put("shareId", 3);

        given(documentationSpec)
            .filter(document("wish-request-cancel-share-by-invalid-member"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestParam)

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_유효하지_않은_쉐어에_위시_취소를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        JSONObject requestParam = new JSONObject();
        requestParam.put("shareId", 222);

        given(documentationSpec)
            .filter(document("wish-request-cancel-invalid-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestParam)

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(ShareExceptionType.SHARE_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(ShareExceptionType.SHARE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ShareExceptionType.SHARE_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_위시_등록하지_않은_쉐어에_위시_취소를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        JSONObject requestParam = new JSONObject();
        requestParam.put("shareId", 4);

        given(documentationSpec)
            .filter(document("wish-re-request-cancel-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestParam)

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(WishExceptionType.SHARE_NOT_WISH.getStatusCode().value())
            .body("errorCode", equalTo(WishExceptionType.SHARE_NOT_WISH.getErrorCode()))
            .body("message", equalTo(WishExceptionType.SHARE_NOT_WISH.getMessage()));
    }

}
