package louie.hanse.shareplate.core.share.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.core.share.exception.InvalidClosedDateTimeException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ClosedDateTime {

    @Column
    private LocalDateTime closedDateTime;

    public ClosedDateTime(LocalDateTime closedDateTime) {
        validateClosedDateTime(closedDateTime);
        this.closedDateTime = closedDateTime;
    }

    public boolean isLeftLessThanAnHour() {
        LocalDateTime leftAnHour = closedDateTime.minusHours(1);
        if (leftAnHour.compareTo(LocalDateTime.now()) < 0) {
            return true;
        }
        return false;
    }

    public boolean isClosed() {
        return isPastTime(closedDateTime);
    }

    public boolean isNotClosed() {
        return !isClosed();
    }

    private void validateClosedDateTime(LocalDateTime closedDateTime) {
        if (Objects.isNull(closedDateTime) || isPastTime(closedDateTime)) {
            throw new InvalidClosedDateTimeException();
        }
    }

    private boolean isPastTime(LocalDateTime closedDateTime) {
        return closedDateTime.isBefore(LocalDateTime.now());
    }
}
