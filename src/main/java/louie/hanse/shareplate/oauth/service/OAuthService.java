package louie.hanse.shareplate.oauth.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.oauth.OAuthProperties;
import louie.hanse.shareplate.oauth.OAuthUserInfo;
import louie.hanse.shareplate.oauth.client.OAuthTokenClient;
import louie.hanse.shareplate.oauth.client.OAuthUserClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthProperties oAuthProperties;
    private final OAuthTokenClient oAuthTokenClient;
    private final OAuthUserClient oAuthUserClient;

    public String getAccessToken(String code) {
        return oAuthTokenClient.getAccessToken("authorization_code",
            oAuthProperties.getClientId(), oAuthProperties.getRedirectUrl(), code)
            .getAccessToken();
    }

    public OAuthUserInfo getUserInfo(String accessToken) {
        return oAuthUserClient.getUserInfo("Bearer " + accessToken);
    }

}
