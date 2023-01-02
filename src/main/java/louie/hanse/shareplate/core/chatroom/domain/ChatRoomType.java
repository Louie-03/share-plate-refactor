package louie.hanse.shareplate.core.chatroom.domain;

import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.ChatRoomExceptionType;

public enum ChatRoomType {
    QUESTION, ENTRY;

    public static ChatRoomType valueOfWithCaseInsensitive(String name) {
        name = name.toUpperCase();

        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new GlobalException(ChatRoomExceptionType.INCORRECT_TYPE_VALUE);
        }
    }

    public boolean isEntry() {
        return this == ENTRY;
    }

    public boolean isQuestion() {
        return this == QUESTION;
    }
}
