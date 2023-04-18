package louie.hanse.shareplate.oauth.client;

import louie.hanse.shareplate.oauth.OAuthUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "oauth-user-client", url = "${oauth.user.api_url}")
public interface OAuthUserClient {

    @PostMapping("/v2/user/me")
    OAuthUserInfo getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);
}
