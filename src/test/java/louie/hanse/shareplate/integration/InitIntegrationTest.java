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
import louie.hanse.shareplate.config.S3MockConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.jdbc.Sql;

@EmbeddedKafka(partitions = 1)
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(S3MockConfig.class)
@Sql("classpath:/data.sql")
public class InitIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    protected JwtProvider jwtProvider;

    @Autowired
    private AsyncConfigurer asyncConfigurer;

    protected RequestSpecification documentationSpec;

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
    }

    private ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return (ThreadPoolTaskExecutor) asyncConfigurer.getAsyncExecutor();
    }

    private ThreadPoolExecutor getThreadPoolExecutor() {
        return getThreadPoolTaskExecutor().getThreadPoolExecutor();
    }

}
