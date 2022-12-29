package louie.hanse.shareplate.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("StringUtils 클래스")
class StringUtilsTest {

    @Nested
    @DisplayName("isBlank 메서드는")
    class Describe_isBlank {

        @Nested
        @DisplayName("null이 주어진다면")
        class Context_with_null {

            @Test
            @DisplayName("true를 반환한다")
            void it_returns_true() {
                assertThat(StringUtils.isBlank(null)).isTrue();
            }
        }

        @Nested
        @DisplayName("빈 문자열이 주어진다면")
        class Context_with_empty_string {

            @Test
            @DisplayName("true를 반환한다")
            void it_returns_true() {
                assertThat(StringUtils.isBlank("")).isTrue();
            }
        }

        @Nested
        @DisplayName("공백이 주어진다면")
        class Context_with_blank_string {

            @Test
            @DisplayName("true를 반환한다")
            void it_returns_true() {
                assertThat(StringUtils.isBlank("   ")).isTrue();
            }
        }
    }

}
