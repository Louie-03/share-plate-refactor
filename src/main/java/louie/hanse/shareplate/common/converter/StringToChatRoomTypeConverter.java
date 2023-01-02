package louie.hanse.shareplate.common.converter;

import louie.hanse.shareplate.core.chatroom.domain.ChatRoomType;
import org.springframework.core.convert.converter.Converter;

public class StringToChatRoomTypeConverter implements Converter<String, ChatRoomType> {

    @Override
    public ChatRoomType convert(String source) {
        return ChatRoomType.valueOfWithCaseInsensitive(source);
    }
}
