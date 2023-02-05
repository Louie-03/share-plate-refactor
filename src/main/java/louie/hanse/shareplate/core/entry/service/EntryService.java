package louie.hanse.shareplate.core.entry.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoom;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoomMember;
import louie.hanse.shareplate.core.entry.domain.Entry;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.core.member.service.MemberService;
import louie.hanse.shareplate.core.notification.domain.ActivityType;
import louie.hanse.shareplate.core.notification.event.ActivityNotificationRegisterEvent;
import louie.hanse.shareplate.core.share.domain.Share;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.EntryExceptionType;
import louie.hanse.shareplate.core.chatroom.repository.ChatRoomMemberRepository;
import louie.hanse.shareplate.core.entry.repository.EntryRepository;
import louie.hanse.shareplate.core.share.service.ShareService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class EntryService {

    private final MemberService memberService;
    private final ShareService shareService;
    private final EntryRepository entryRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Long entry(Long shareId, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = shareService.findByIdOrElseThrow(shareId);

        share.isCanceledThrowException();
        if (share.isClosed()) {
            throw new GlobalException(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_JOIN);
        }
        if (isExistEntry(shareId, memberId)) {
            throw new GlobalException(EntryExceptionType.SHARE_ALREADY_JOINED);
        }
        share.recruitmentQuotaExceededThrowException();

        Entry entry = new Entry(share, member);
        entryRepository.save(entry);
        ChatRoom chatRoom = share.getEntryChatRoom();
        ChatRoomMember chatRoomMember = new ChatRoomMember(member, chatRoom);
        chatRoomMemberRepository.save(chatRoomMember);

        publisher.publishEvent(
            new ActivityNotificationRegisterEvent(shareId, memberId, ActivityType.ENTRY));

        return entry.getId();
    }

    @Transactional
    public void cancel(Long shareId, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = shareService.findWithWriterByIdOrElseThrow(shareId);

        share.isCanceledThrowException();
        if (!isExistEntry(shareId, memberId)) {
            throw new GlobalException(EntryExceptionType.SHARE_NOT_JOINED);
        }
        if (share.isClosed()) {
            throw new GlobalException(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_CANCEL);
        }
        if (share.isLeftLessThanAnHour()) {
            throw new GlobalException(EntryExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME);
        }
        if (share.isWriter(member)) {
            throw new GlobalException(EntryExceptionType.SHARE_WRITER_CANNOT_ENTRY_CANCEL);
        }
        entryRepository.deleteByMemberIdAndShareId(memberId, shareId);
        chatRoomMemberRepository.deleteByMemberIdAndChatRoomId(
            memberId, share.getEntryChatRoom().getId());

        publisher.publishEvent(
            new ActivityNotificationRegisterEvent(shareId, memberId, ActivityType.ENTRY_CANCEL));
    }

    public List<Long> getIdList(Long memberId) {
        memberService.findByIdOrElseThrow(memberId);
        return entryRepository.findAllByMemberId(memberId)
            .stream().map(Entry::getId).collect(Collectors.toList());
    }

    private boolean isExistEntry(Long shareId, Long memberId) {
        return entryRepository.existsByMemberIdAndShareId(memberId, shareId);
    }
}
