package louie.hanse.shareplate.common.validator.share;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import louie.hanse.shareplate.common.exception.type.ShareExceptionType;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class ShareImageValidator implements ConstraintValidator<ValidShareImage, MultipartFile> {

    private static final List<String> enableContentTypes = createEnableContentTypes();

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        String contentType = multipartFile.getContentType();
        if (!enableContentTypes.contains(contentType)) {
            context.buildConstraintViolationWithTemplate(
                ShareExceptionType.NOT_SUPPORT_IMAGE_TYPE.getMessage())
                .addConstraintViolation();
            return false;
        }
        return true;
    }

    private static List<String> createEnableContentTypes() {
        return List.of(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE);
    }
}
