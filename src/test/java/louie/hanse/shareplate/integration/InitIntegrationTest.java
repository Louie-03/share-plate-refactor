package louie.hanse.shareplate.integration;

import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.DATE;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.http.HttpHeaders.TRANSFER_ENCODING;
import static org.springframework.http.HttpHeaders.VARY;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import louie.hanse.shareplate.common.jwt.JwtProvider;
import louie.hanse.shareplate.config.RabbitTestConfig;
import louie.hanse.shareplate.config.S3MockConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.extension.ExtendWith;
 import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.RabbitMQContainer;

@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({S3MockConfig.class, RabbitTestConfig.class})
@Sql("classpath:/data.sql")
public class InitIntegrationTest {

    private static final RabbitMQContainer RABBIT_CONTAINER = new RabbitMQContainer(
        "rabbitmq:3-management");

    @LocalServerPort
    int port;

    @Autowired
    protected JwtProvider jwtProvider;

    @Autowired
    private AsyncConfigurer asyncConfigurer;

    @Autowired
    RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    protected RequestSpecification documentationSpec;

    static {
        RABBIT_CONTAINER.start();
    }

    @DynamicPropertySource
    static void overrideConfiguration(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", RABBIT_CONTAINER::getHost);
        registry.add("spring.rabbitmq.port", RABBIT_CONTAINER::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBIT_CONTAINER::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBIT_CONTAINER::getAdminPassword);
        registry.add("spring.rabbitmq.ssl.enabled", () -> false);
    }

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        documentationSpec = new RequestSpecBuilder()
            .addFilter(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(
                        prettyPrint(),
                        removeHeaders(HOST, CONTENT_LENGTH))
                    .withResponseDefaults(
                        prettyPrint(),
                        removeHeaders(CONTENT_LENGTH, CONNECTION, DATE, TRANSFER_ENCODING,
                            "Keep-Alive", VARY))
            )
            .build();
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        ThreadPoolTaskExecutor executor = getThreadPoolTaskExecutor();
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
        threadPoolExecutor.shutdown();
        if (!threadPoolExecutor.awaitTermination(3000, TimeUnit.MILLISECONDS)) {
            executor.shutdown();
        }
        executor.initialize();

        rabbitListenerEndpointRegistry.stop();
        rabbitListenerEndpointRegistry.start();
    }

    private ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return (ThreadPoolTaskExecutor) asyncConfigurer.getAsyncExecutor();
    }

    private ThreadPoolExecutor getThreadPoolExecutor() {
        return getThreadPoolTaskExecutor().getThreadPoolExecutor();
    }

}
