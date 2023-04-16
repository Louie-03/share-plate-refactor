package louie.hanse.shareplate.oauth.client;

import louie.hanse.shareplate.oauth.OAuthAccessToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "oauth-token-client", url = "https://kauth.kakao.com")
public interface OAuthTokenClient {

    @PostMapping("/oauth/token")
    OAuthAccessToken getAccessToken(
        @RequestParam("grant_type") String grantType,
        @RequestParam("client_id") String clientId,
        @RequestParam("redirect_uri") String redirectUri,
        @RequestParam("code") String code
    );
}
