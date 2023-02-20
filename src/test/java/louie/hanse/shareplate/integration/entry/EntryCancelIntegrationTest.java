package louie.hanse.shareplate.integration.entry;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.common.exception.type.EntryExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME;
import static louie.hanse.shareplate.common.exception.type.EntryExceptionType.SHARE_NOT_JOINED;
import static louie.hanse.shareplate.common.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.PATH_VARIABLE_EMPTY_SHARE_ID;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.SHARE_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.SHARE_IS_CANCELED;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.SHARE_NOT_FOUND;
import static louie.hanse.shareplate.integration.entry.utils.EntryIntegrationTestUtils.createShareRegisterRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.io.IOException;
import java.time.LocalDateTime;
import louie.hanse.shareplate.core.share.dto.request.ShareRegisterRequest;
import louie.hanse.shareplate.core.share.service.ShareService;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("쉐어 참여 취소 통합테스트")
class EntryCancelIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;


    @Test
    void 회원이_쉐어_참여를_취소한다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("entry-cancel-delete"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 2)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void path_variable의_쉐어_id_null값일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("entry-cancel-delete-failed-by-path-variable-empty-share-id"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", " ")

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(PATH_VARIABLE_EMPTY_SHARE_ID.getStatusCode().value())
            .body("errorCode", equalTo(PATH_VARIABLE_EMPTY_SHARE_ID.getErrorCode()))
            .body("message", equalTo(PATH_VARIABLE_EMPTY_SHARE_ID.getMessage()));
    }

    @Test
    void 쉐어_id가_양수가_아닐_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("entry-cancel-delete-failed-by-share-id-not-positive"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", -2)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(SHARE_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("entry-cancel-delete-failed-by-member-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 2)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 존재하지_않은_쉐어_id일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("entry-cancel-delete-failed-by-share-id-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 2222)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(SHARE_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(SHARE_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_참여되어_있지_않은_쉐어일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("entry-cancel-delete-failed-by-share-not-joined"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 4)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(SHARE_NOT_JOINED.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_NOT_JOINED.getErrorCode()))
            .body("message", equalTo(SHARE_NOT_JOINED.getMessage()));
    }

    @Test
    void 회원이_취소된_쉐어에_요청할_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("entry-cancel-delete-failed-by-already-share-is-canceled"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 6)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(SHARE_IS_CANCELED.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_IS_CANCELED.getErrorCode()))
            .body("message", equalTo(SHARE_IS_CANCELED.getMessage()));
    }

    @Test
    void 한시간_미만_남은_쉐어일_경우_예외를_발생시킨다() throws IOException {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        ShareRegisterRequest request = createShareRegisterRequest(LocalDateTime.now().plusMinutes(30));

        Long shareId = shareService.register(request, 2355841047L).get("id");

        given(documentationSpec)
            .filter(document("entry-cancel-delete-failed-by-close-to-tje-closed-date-time"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", shareId)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(CLOSE_TO_THE_CLOSED_DATE_TIME.getStatusCode().value())
            .body("errorCode", equalTo(CLOSE_TO_THE_CLOSED_DATE_TIME.getErrorCode()))
            .body("message", equalTo(CLOSE_TO_THE_CLOSED_DATE_TIME.getMessage()));
    }

//    @Test
//    void 모집_시간이_지난_쉐어일_경우_예외를_발생시킨다() throws IOException {
//        String accessToken = jwtProvider.createAccessToken(2355841047L);
//
//        ShareRegisterRequest request = getShareRegisterRequest(LocalDateTime.now().minusHours(3));
//        Long shareId = shareService.register(request, 2355841047L).get("id");
//
//        given(documentationSpec)
//            .filter(document("entry-cancel-delete-failed-by-closed-date-time-was-passed-cancel"))
//            .contentType(ContentType.JSON)
//            .header(AUTHORIZATION, accessToken)
//            .pathParam("shareId", shareId)
//
//            .when()
//            .delete("/shares/{shareId}/entry")
//
//            .then()
//            .statusCode(CLOSED_DATE_TIME_HAS_PASSED_NOT_CANCEL.getStatusCode().value())
//            .body("errorCode", equalTo(CLOSED_DATE_TIME_HAS_PASSED_NOT_CANCEL.getErrorCode()))
//            .body("message", equalTo(CLOSED_DATE_TIME_HAS_PASSED_NOT_CANCEL.getMessage()));
//    }
}
