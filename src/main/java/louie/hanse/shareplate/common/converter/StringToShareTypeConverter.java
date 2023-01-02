package louie.hanse.shareplate.common.converter;

import louie.hanse.shareplate.core.share.domain.ShareType;
import org.springframework.core.convert.converter.Converter;

public class StringToShareTypeConverter implements Converter<String, ShareType> {

    @Override
    public ShareType convert(String source) {
        return ShareType.valueOfWithCaseInsensitive(source);
    }
}
