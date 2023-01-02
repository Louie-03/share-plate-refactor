package louie.hanse.shareplate.core.share.domain;

import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.ShareExceptionType;

public enum ShareType {
    DELIVERY, INGREDIENT;

    public static ShareType valueOfWithCaseInsensitive(String name) {
        name = name.toUpperCase();

        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new GlobalException(ShareExceptionType.INCORRECT_TYPE_VALUE);
        }
    }
}
