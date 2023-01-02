package louie.hanse.shareplate.core.chat.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import louie.hanse.shareplate.core.chat.domain.Chat;
import louie.hanse.shareplate.core.share.domain.Share;
import louie.hanse.shareplate.core.member.domain.Member;

@Getter
public class ChatDetailResponse {

    private String contents;
    private String writer;
    private String writerThumbnailImageUrl;
    private LocalDateTime writtenDateTime;
    private boolean writtenByMe;
    private boolean shareWrittenByMe;

    public ChatDetailResponse(Chat chat, Member member, Share share) {
        Member chatWriter = chat.getWriter();
        this.contents = chat.getContents();
        this.writer = chatWriter.getNickname();
        this.writerThumbnailImageUrl = chatWriter.getThumbnailImageUrl();
        this.writtenDateTime = chat.getWrittenDateTime();
        this.writtenByMe = member.equals(chatWriter);
        this.shareWrittenByMe = share.isWriter(chatWriter);
    }
}
