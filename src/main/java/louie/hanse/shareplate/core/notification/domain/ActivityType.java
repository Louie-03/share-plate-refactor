package louie.hanse.shareplate.core.notification.domain;

public enum ActivityType {
    ENTRY, DEADLINE, SHARE_CANCEL, ENTRY_CANCEL, QUESTION_CHATROOM;

    public boolean isDeadLine() {
        return this == DEADLINE;
    }
}
