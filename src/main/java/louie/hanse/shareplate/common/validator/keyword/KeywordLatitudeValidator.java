package louie.hanse.shareplate.common.validator.keyword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import louie.hanse.shareplate.common.exception.type.KeywordExceptionType;
import louie.hanse.shareplate.common.exception.type.ShareExceptionType;
import org.springframework.util.ObjectUtils;

public class KeywordLatitudeValidator implements ConstraintValidator<ValidKeywordLatitude, Double> {

    @Override
    public boolean isValid(Double latitude, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (ObjectUtils.isEmpty(latitude)) {
            context.buildConstraintViolationWithTemplate(
                    KeywordExceptionType.EMPTY_KEYWORD_INFO.getMessage())
                .addConstraintViolation();
            return false;
        }

        if (!(33.1 <= latitude && latitude <= 38.45)) {
            context.buildConstraintViolationWithTemplate(
                    ShareExceptionType.OUT_OF_SCOPE_FOR_KOREA.getMessage())
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
