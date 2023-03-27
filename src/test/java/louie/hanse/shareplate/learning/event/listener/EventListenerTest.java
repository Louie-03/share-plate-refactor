package louie.hanse.shareplate.learning.event.listener;

import static louie.hanse.shareplate.integration.entry.utils.EntryIntegrationTestUtils.createShareRegisterRequest;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import louie.hanse.shareplate.core.share.domain.Share;
import louie.hanse.shareplate.core.share.dto.request.ShareRegisterRequest;
import louie.hanse.shareplate.core.share.service.ShareService;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EventListenerTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

    @Autowired
    EntityManager entityManager;

    @Test
    void name() throws IOException {
        ShareRegisterRequest shareRegisterRequest = createShareRegisterRequest(
            LocalDateTime.now().plusDays(3));

//        assertThatThrownBy(() -> shareService.register(shareRegisterRequest, 2370842997L))
//            .isExactlyInstanceOf(RuntimeException.class);

        shareService.register(shareRegisterRequest, 2370842997L);

        assertThatThrownBy(() ->
            entityManager.createQuery("select s from Share s where s.title.title = :title",
                    Share.class)
                .setParameter("title", shareRegisterRequest.getTitle())
                .getSingleResult()
        ).isExactlyInstanceOf(NoResultException.class);

    }

}
