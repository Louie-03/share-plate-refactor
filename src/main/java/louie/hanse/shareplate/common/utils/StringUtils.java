package louie.hanse.shareplate.common.utils;

import java.util.Objects;

public class StringUtils {

    public static boolean isBlank(String string) {
        return Objects.isNull(string) || string.isBlank();
    }
}
