package louie.hanse.shareplate.integration.share;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.MULTIPART;
import static louie.hanse.shareplate.common.exception.invalid.type.InvalidExceptionType.INVALID_CLOSED_DATE_TIME;
import static louie.hanse.shareplate.common.exception.invalid.type.InvalidExceptionType.INVALID_LATITUDE;
import static louie.hanse.shareplate.common.exception.invalid.type.InvalidExceptionType.INVALID_LONGITUDE;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.EMPTY_SHARE_INFO;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.IMAGE_LIMIT_EXCEEDED;
import static louie.hanse.shareplate.common.exception.type.ShareExceptionType.NOT_SUPPORT_IMAGE_TYPE;
import static louie.hanse.shareplate.integration.share.utils.ShareIntegrationTestUtils.createMultiPartSpecification;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("쉐어 등록 통합 테스트")
class ShareRegisterIntegrationTest extends InitIntegrationTest {

    @Test
    void 음식_공유를_하기_위해_쉐어를_등록한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-register-post"))
            .header(AUTHORIZATION, accessToken)
            .contentType(MULTIPART)
            .multiPart("images", "test1.jpg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test1.jpeg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.png", "def".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "제목"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그1"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그2"))
            .multiPart(createMultiPartSpecification("locationGuide", "강남역 파출소 앞"))
            .multiPart(createMultiPartSpecification("location", "강남역"))
            .multiPart(createMultiPartSpecification("description", "설명"))
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
            .post("/shares")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("entryId", notNullValue());
    }

//    TODO 어떤 필드가 검증에 실패했는지도 메세지로 나타내기, closedDateTime 패턴 형식까지 검증하기
    @Test
    void 이미지를_입력하지_않은_경우_예외가_발생한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-register-post-failed-by-empty-share-info"))
            .header(AUTHORIZATION, accessToken)
            .contentType(MULTIPART)
            .multiPart(createMultiPartSpecification("title", "제목"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그1"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그2"))
            .multiPart(createMultiPartSpecification("locationGuide", "강남역 파출소 앞"))
            .multiPart(createMultiPartSpecification("location", "강남역"))
            .multiPart(createMultiPartSpecification("description", "설명"))
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
            .post("/shares")

            .then()
            .statusCode(EMPTY_SHARE_INFO.getStatusCode().value())
            .body("errorCode", equalTo(EMPTY_SHARE_INFO.getErrorCode()))
            .body("message", equalTo(EMPTY_SHARE_INFO.getMessage()));
    }

    @Test
    void 지원하지_않은_이미지_형식인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-register-post-failed-by-not-support-image-type"))
            .header(AUTHORIZATION, accessToken)
            .contentType(MULTIPART)
            .multiPart("images", "test1.txt", "abc".getBytes(), TEXT_PLAIN_VALUE)
            .multiPart("images", "test2.pdf", "def".getBytes(), APPLICATION_PDF_VALUE)
            .multiPart(createMultiPartSpecification("title", "제목"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그1"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그2"))
            .multiPart(createMultiPartSpecification("locationGuide", "강남역 파출소 앞"))
            .multiPart(createMultiPartSpecification("location", "강남역"))
            .multiPart(createMultiPartSpecification("description", "설명"))
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
            .post("/shares")

            .then()
            .statusCode(NOT_SUPPORT_IMAGE_TYPE.getStatusCode().value())
            .body("errorCode", equalTo(NOT_SUPPORT_IMAGE_TYPE.getErrorCode()))
            .body("message", equalTo(NOT_SUPPORT_IMAGE_TYPE.getMessage()));
    }

    @Test
    void 이미지의_개수가_5개가_넘는다면_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-register-post-failed-by-image-limit_exceeded"))
            .header(AUTHORIZATION, accessToken)
            .contentType(MULTIPART)
            .multiPart("images", "test1.jpg", "abc1".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.jpg", "abc2".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test3.jpg", "abc3".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test4.jpg", "abc4".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test5.jpg", "abc5".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test6.jpg", "abc6".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart(createMultiPartSpecification("title", "제목"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그1"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그2"))
            .multiPart(createMultiPartSpecification("locationGuide", "강남역 파출소 앞"))
            .multiPart(createMultiPartSpecification("location", "강남역"))
            .multiPart(createMultiPartSpecification("description", "설명"))
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
            .post("/shares")

            .then()
            .statusCode(IMAGE_LIMIT_EXCEEDED.getStatusCode().value())
            .body("errorCode", equalTo(IMAGE_LIMIT_EXCEEDED.getErrorCode()))
            .body("message", equalTo(IMAGE_LIMIT_EXCEEDED.getMessage()));
    }

    @Test
    void 대한민국의_위도_범위를_벗어난_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-register-post-failed-by-out-of-scope-for-korea"))
            .header(AUTHORIZATION, accessToken)
            .contentType(MULTIPART)
            .multiPart("images", "test1.jpg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test1.jpeg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.png", "def".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "제목"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그1"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그2"))
            .multiPart(createMultiPartSpecification("locationGuide", "강남역 파출소 앞"))
            .multiPart(createMultiPartSpecification("location", "강남역"))
            .multiPart(createMultiPartSpecification("description", "설명"))
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
            .post("/shares")

            .then()
            .statusCode(INVALID_LATITUDE.getStatusCode())
            .body("errorCode", equalTo(INVALID_LATITUDE.getErrorCode()));
    }

    @Test
    void 대한민국의_경도_범위를_벗어난_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-register-post-failed-by-out-of-scope-for-korea"))
            .header(AUTHORIZATION, accessToken)
            .contentType(MULTIPART)
            .multiPart("images", "test1.jpg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test1.jpeg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.png", "def".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "제목"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그1"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그2"))
            .multiPart(createMultiPartSpecification("locationGuide", "강남역 파출소 앞"))
            .multiPart(createMultiPartSpecification("location", "강남역"))
            .multiPart(createMultiPartSpecification("description", "설명"))
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
            .post("/shares")

            .then()
            .statusCode(INVALID_LONGITUDE.getStatusCode())
            .body("errorCode", equalTo(INVALID_LONGITUDE.getErrorCode()));
    }

    @Test
    void 마감_시간이_과거인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-register-post-failed-by-past-closed-date-time"))
            .header(AUTHORIZATION, accessToken)
            .contentType(MULTIPART)
            .multiPart("images", "test1.jpg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test1.jpeg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.png", "def".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "제목"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그1"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그2"))
            .multiPart(createMultiPartSpecification("locationGuide", "강남역 파출소 앞"))
            .multiPart(createMultiPartSpecification("location", "강남역"))
            .multiPart(createMultiPartSpecification("description", "설명"))
            .formParam("type", "delivery")
            .formParam("price", 10000)
            .formParam("originalPrice", 30000)
            .formParam("recruitment", 3)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", true)
            .formParam("latitude", 37.524159)
            .formParam("longitude", 126.872879)
            .formParam("closedDateTime", "2021-12-30 14:00")

            .when()
            .post("/shares")

            .then()
            .statusCode(INVALID_CLOSED_DATE_TIME.getStatusCode())
            .body("errorCode", equalTo(INVALID_CLOSED_DATE_TIME.getErrorCode()));
    }
}
