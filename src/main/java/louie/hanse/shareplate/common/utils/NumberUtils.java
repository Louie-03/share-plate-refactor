package louie.hanse.shareplate.common.utils;

import java.util.Objects;

public class NumberUtils {

    public static boolean isNullOrNotPositive(Integer integer) {
        return Objects.isNull(integer) || integer < 1;
    }
}
