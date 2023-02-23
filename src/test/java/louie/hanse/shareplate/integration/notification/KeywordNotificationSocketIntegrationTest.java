package louie.hanse.shareplate.integration.notification;

import static louie.hanse.shareplate.integration.entry.utils.EntryIntegrationTestUtils.createShareRegisterRequest;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import louie.hanse.shareplate.core.notification.dto.response.KeywordNotificationResponse;
import louie.hanse.shareplate.core.share.dto.request.ShareRegisterRequest;
import louie.hanse.shareplate.core.share.service.ShareService;
import louie.hanse.shareplate.integration.InitSocketIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("키워드 알림 통합 테스트")
class KeywordNotificationSocketIntegrationTest extends InitSocketIntegrationTest {

    @Autowired
    ShareService shareService;

//    TODO : shareId 검증하도록 구현
    @Test
    void 키워드에_등록한_내용이_포함된_쉐어가_등록된_경우_실시간으로_키워드_알림을_전송한다()
        throws ExecutionException, InterruptedException, TimeoutException, IOException {

//        given
        Long keywordId = 10L;
        String destination = "/queue/notifications/keywords/" + keywordId;
        String subscribeAccessToken = jwtProvider.createAccessToken(2355841047L);

        stompSession.subscribe(createStompHeaders(subscribeAccessToken, destination),
            getStompSessionHandlerAdapter(KeywordNotificationResponse.class));

//        when
        ShareRegisterRequest shareRegisterRequest = createShareRegisterRequest(
            LocalDateTime.now().plusDays(3));
        shareService.register(shareRegisterRequest, 2370842997L);

//        then
        KeywordNotificationResponse result = (KeywordNotificationResponse) completableFuture
            .get(3, TimeUnit.SECONDS);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getShareTitle()).isEqualTo(shareRegisterRequest.getTitle());
        assertThat(result.getShareLocation()).isEqualTo(shareRegisterRequest.getLocation());
        assertThat(result.getShareThumbnailImageUrl()).contains("http");
        assertThat(result.getNotificationCreatedDateTime()).isBefore(LocalDateTime.now());
    }
}
