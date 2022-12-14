package louie.hanse.shareplate.integration.share;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.MULTIPART;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_EDIT;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.EMPTY_SHARE_INFO;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.IMAGE_LIMIT_EXCEEDED;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.IS_NOT_WRITER;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.NOT_SUPPORT_IMAGE_TYPE;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.OUT_OF_SCOPE_FOR_KOREA;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.PAST_CLOSED_DATE_TIME;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.PATH_VARIABLE_EMPTY_SHARE_ID;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.SHARE_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.SHARE_INFO_IS_NEGATIVE;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.SHARE_IS_CANCELED;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.SHARE_IS_CLOSED;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.SHARE_NOT_FOUND;
import static louie.hanse.shareplate.integration.entry.utils.EntryIntegrationTestUtils.getShareRegisterRequest;
import static louie.hanse.shareplate.integration.share.utils.ShareIntegrationTestUtils.createMultiPartSpecification;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import java.io.IOException;
import java.time.LocalDateTime;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.core.share.service.ShareService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("?????? ?????? ?????? ?????????")
class ShareEditIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

    @Test
    void ?????????_?????????_?????????_????????????() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("share-edit-put"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "????????? test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "????????? test2.png", "fhgij".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "????????? ??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "????????? ??????"))
            .formParam("type", "ingredient")
            .formParam("price", 13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2023-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void ????????????_????????????_??????_??????_?????????_????????????() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-empty-share-info"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart(createMultiPartSpecification("title", "????????? ??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "????????? ??????"))
            .formParam("type", "ingredient")
            .formParam("price", 13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2023-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(EMPTY_SHARE_INFO.getStatusCode().value())
            .body("errorCode", equalTo(EMPTY_SHARE_INFO.getErrorCode()))
            .body("message", equalTo(EMPTY_SHARE_INFO.getMessage()));
    }

    @Test
    void ????????????_??????_?????????_?????????_??????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-not-support-image-type"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "????????? test1.txt", "abcde".getBytes(), TEXT_PLAIN_VALUE)
            .multiPart("images", "????????? test2.txt", "fhgij".getBytes(), TEXT_PLAIN_VALUE)
            .multiPart(createMultiPartSpecification("title", "????????? ??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "????????? ??????"))
            .formParam("type", "ingredient")
            .formParam("price", 13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2023-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(NOT_SUPPORT_IMAGE_TYPE.getStatusCode().value())
            .body("errorCode", equalTo(NOT_SUPPORT_IMAGE_TYPE.getErrorCode()))
            .body("message", equalTo(NOT_SUPPORT_IMAGE_TYPE.getMessage()));
    }

    @Test
    void ????????????_?????????_5??????_????????????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-image-limit-exceeded"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "test1.jpg", "abc1".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.jpg", "abc2".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test3.jpg", "abc3".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test4.jpg", "abc4".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test5.jpg", "abc5".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test6.jpg", "abc6".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart(createMultiPartSpecification("title", "??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "??????"))
            .formParam("type", "delivery")
            .formParam("price", 10000)
            .formParam("originalPrice", 30000)
            .formParam("recruitment", 3)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", true)
            .formParam("latitude", 37.524159)
            .formParam("longitude", 126.872879)
            .formParam("closedDateTime", "2023-12-30 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(IMAGE_LIMIT_EXCEEDED.getStatusCode().value())
            .body("errorCode", equalTo(IMAGE_LIMIT_EXCEEDED.getErrorCode()))
            .body("message", equalTo(IMAGE_LIMIT_EXCEEDED.getMessage()));
    }

    @Test
    void ???????????????_??????_?????????_?????????_??????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-out-of-scope-for-korea"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "test1.jpg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test1.jpeg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.png", "def".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "??????"))
            .formParam("type", "delivery")
            .formParam("price", 10000)
            .formParam("originalPrice", 30000)
            .formParam("recruitment", 3)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", true)
            .formParam("latitude", 39)
            .formParam("longitude", 126.872879)
            .formParam("closedDateTime", "2023-12-30 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(OUT_OF_SCOPE_FOR_KOREA.getStatusCode().value())
            .body("errorCode", equalTo(OUT_OF_SCOPE_FOR_KOREA.getErrorCode()))
            .body("message", equalTo(OUT_OF_SCOPE_FOR_KOREA.getMessage()));
    }

    @Test
    void ???????????????_??????_?????????_?????????_??????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-out-of-scope-for-korea"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "test1.jpg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test1.jpeg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.png", "def".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "??????"))
            .formParam("type", "delivery")
            .formParam("price", 10000)
            .formParam("originalPrice", 30000)
            .formParam("recruitment", 3)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", true)
            .formParam("latitude", 37.524159)
            .formParam("longitude", 123)
            .formParam("closedDateTime", "2023-12-30 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(OUT_OF_SCOPE_FOR_KOREA.getStatusCode().value())
            .body("errorCode", equalTo(OUT_OF_SCOPE_FOR_KOREA.getErrorCode()))
            .body("message", equalTo(OUT_OF_SCOPE_FOR_KOREA.getMessage()));
    }

    @Test
    void ??????_?????????_?????????_??????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-past-closed-date-time"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "test1.jpg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test1.jpeg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.png", "def".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "??????"))
            .formParam("type", "delivery")
            .formParam("price", 10000)
            .formParam("originalPrice", 30000)
            .formParam("recruitment", 3)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", true)
            .formParam("latitude", 37.524159)
            .formParam("longitude", 126.872879)
            .formParam("closedDateTime", "2000-12-30 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(PAST_CLOSED_DATE_TIME.getStatusCode().value())
            .body("errorCode", equalTo(PAST_CLOSED_DATE_TIME.getErrorCode()))
            .body("message", equalTo(PAST_CLOSED_DATE_TIME.getMessage()));
    }

    @Test
    void ??????_?????????_?????????_??????_??????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-share-info-is-negative"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "????????? test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "????????? test2.png", "fhgij".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "????????? ??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "????????? ??????"))
            .formParam("type", "ingredient")
            .formParam("price", 13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 0)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2023-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(SHARE_INFO_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_INFO_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_INFO_IS_NEGATIVE.getMessage()));
    }

    @Test
    void price???_?????????_??????_??????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-share-info-is-negative"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "????????? test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "????????? test2.png", "fhgij".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "????????? ??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "????????? ??????"))
            .formParam("type", "ingredient")
            .formParam("price", -13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2023-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(SHARE_INFO_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_INFO_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_INFO_IS_NEGATIVE.getMessage()));
    }

    @Test
    void originalPrice???_?????????_??????_??????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-share-info-is-negative"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "????????? test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "????????? test2.png", "fhgij".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "????????? ??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "????????? ??????"))
            .formParam("type", "ingredient")
            .formParam("price", 13000)
            .formParam("originalPrice", -1000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2023-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(SHARE_INFO_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_INFO_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_INFO_IS_NEGATIVE.getMessage()));
    }

    @Test
    void ??????_id???_null??????_??????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-share-id-null"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", "  ")
            .multiPart("images", "????????? test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "????????? test2.png", "fhgij".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "????????? ??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "????????? ??????"))
            .formParam("type", "ingredient")
            .formParam("price", 13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2023-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(PATH_VARIABLE_EMPTY_SHARE_ID.getStatusCode().value())
            .body("errorCode", equalTo(PATH_VARIABLE_EMPTY_SHARE_ID.getErrorCode()))
            .body("message", equalTo(PATH_VARIABLE_EMPTY_SHARE_ID.getMessage()));
    }

    @Test
    void ??????_id???_?????????_??????_??????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-share-id-not-positive"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", -3)
            .multiPart("images", "????????? test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "????????? test2.png", "fhgij".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "????????? ??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "????????? ??????"))
            .formParam("type", "ingredient")
            .formParam("price", 13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2023-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(SHARE_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void ????????????_??????_?????????_??????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-share-not-found"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 33333333333L)
            .multiPart("images", "????????? test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "????????? test2.png", "fhgij".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "????????? ??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "????????? ??????"))
            .formParam("type", "ingredient")
            .formParam("price", 13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2023-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(SHARE_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(SHARE_NOT_FOUND.getMessage()));
    }

    @Test
    void ??????_????????????_??????_??????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-not-share-written-by-me"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "????????? test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "????????? test2.png", "fhgij".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "????????? ??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "????????? ??????"))
            .formParam("type", "ingredient")
            .formParam("price", 13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2023-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(IS_NOT_WRITER.getStatusCode().value())
            .body("errorCode", equalTo(IS_NOT_WRITER.getErrorCode()))
            .body("message", equalTo(IS_NOT_WRITER.getMessage()));
    }

    @Test
    void ?????????_?????????_??????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-already-share-is-closed"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 5)
            .multiPart("images", "????????? test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "????????? test2.png", "fhgij".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "????????? ??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "????????? ??????"))
            .formParam("type", "ingredient")
            .formParam("price", 13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2023-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(SHARE_IS_CLOSED.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_IS_CLOSED.getErrorCode()))
            .body("message", equalTo(SHARE_IS_CLOSED.getMessage()));
    }

    @Test
    void ?????????_?????????_??????_?????????_???????????????() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-already-share-is-canceled"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .multiPart("images", "????????? test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "????????? test2.png", "fhgij".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "????????? ??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "????????? ??????"))
            .formParam("type", "ingredient")
            .formParam("price", 13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2023-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(SHARE_IS_CANCELED.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_IS_CANCELED.getErrorCode()))
            .body("message", equalTo(SHARE_IS_CANCELED.getMessage()));
    }

    @Test
    void ??????_?????????_?????????_????????????_??????_?????????_??????_?????????_???????????????() throws IOException {
        Long writerId = 2370842997L;

        Long shareId = shareService.register(
            getShareRegisterRequest(LocalDateTime.now().plusMinutes(30)), writerId).get("id");

        String accessToken = jwtProvider.createAccessToken(writerId);

        given(documentationSpec)
            .filter(document("share-edit-put-failed-by-close-to-the-closed-date-time"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", shareId)
            .multiPart("images", "????????? test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "????????? test2.png", "fhgij".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "????????? ??????"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????1"))
            .multiPart(createMultiPartSpecification("hashtags", "????????? ????????????2"))
            .multiPart(createMultiPartSpecification("locationGuide", "????????? ????????? ???"))
            .multiPart(createMultiPartSpecification("location", "?????????"))
            .multiPart(createMultiPartSpecification("description", "????????? ??????"))
            .formParam("type", "ingredient")
            .formParam("price", 13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2023-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_EDIT.getStatusCode().value())
            .body("errorCode", equalTo(CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_EDIT.getErrorCode()))
            .body("message", equalTo(CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_EDIT.getMessage()));
    }
}
