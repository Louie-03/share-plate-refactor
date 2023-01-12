package louie.hanse.shareplate.core.share.domain;

import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.ShareExceptionType;

public enum MineType {
    ENTRY, WRITER, WISH;

    public static MineType valueOfWithCaseInsensitive(String name) {
        name = name.toUpperCase();

        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new GlobalException(ShareExceptionType.INCORRECT_MINE_VALUE);
        }
    }

    public boolean isEntry() {
        return this == ENTRY;
    }

    public boolean isWriter() {
        return this == WRITER;
    }

    public boolean isWish() {
        return this == WISH;
    }
}
