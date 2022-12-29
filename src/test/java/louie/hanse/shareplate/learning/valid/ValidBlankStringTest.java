package louie.hanse.shareplate.learning.valid;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.util.ObjectUtils;

class ValidBlankStringTest {

    String emptyString = "";
    String blankString = " ";

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

}
