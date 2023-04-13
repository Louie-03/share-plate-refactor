package louie.hanse.shareplate.learning.valid;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;
import louie.hanse.shareplate.oauth.OAuthProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
    "oauth.access_token_api_url=http://localhost:${wiremock.server.port}",
    "oauth.login_form_url=http://localhost:${wiremock.server.port}",
    "oauth.user_api_url=http://localhost:${wiremock.server.port}"
})
@SpringBootTest
class ValidBlankStringTest {

    String emptyString = "";
    String blankString = " ";

    @Autowired
    OAuthProperties oAuthProperties;

    @BeforeEach
    void setUp() {
        stubFor(get("/")
            .withHeader(HttpHeaders.CONTENT_TYPE,
                equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withBody("form")
            )
        );
        stubFor(get("/")
            .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withBody("json")
            )
        );
    }

    @Test
    void stringIsEmpty() {
        assertThat(emptyString.isEmpty()).isTrue();
        assertThat(blankString.isEmpty()).isFalse();
    }

    @Test
    void stringIsBlank() {
        assertThat(emptyString.isBlank()).isTrue();
        assertThat(blankString.isBlank()).isTrue();
    }

    @Test
    void objectUtilsIsEmpty() {
        assertThat(ObjectUtils.isEmpty(emptyString)).isTrue();
        assertThat(ObjectUtils.isEmpty(blankString)).isFalse();
        assertThat(ObjectUtils.isEmpty(null)).isTrue();
    }

    @Test
    void name() {
        System.out.println("accessTokenApiUrl = " + oAuthProperties.getAccessTokenApiUrl());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(oAuthProperties.getAccessTokenApiUrl(), Void.class);
    }

    @Test
    @DisplayName("유저로부터 로그인 요청이 들어오면 유저의 정보를 저장하고 토큰을 발급한다.")
    void user_login_success() {
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(Map.of("code", "code"))
            .accept(MediaType.APPLICATION_JSON_VALUE)

            .when()
            .post("/login")

            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("thumbnailImageUrl", notNullValue())
            .header("Access-Token", notNullValue())
            .header("Refresh-Token", notNullValue());
    }

    @Test
    void name2() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        ResponseEntity<String> jsonResult = restTemplate.exchange(
            oAuthProperties.getAccessTokenApiUrl(), HttpMethod.GET,
            new HttpEntity<>(jsonHeaders), String.class);

        HttpHeaders formHeaders = new HttpHeaders();
        formHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        ResponseEntity<String> formResult = restTemplate.exchange(
            oAuthProperties.getAccessTokenApiUrl(), HttpMethod.GET,
            new HttpEntity<>(formHeaders), String.class);

        assertThat(jsonResult.getBody()).isEqualTo("json");
        assertThat(formResult.getBody()).isEqualTo("form");
    }
}
