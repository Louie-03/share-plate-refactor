package louie.hanse.shareplate.core.wish.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.wish.domain.Wish;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.core.member.service.MemberService;
import louie.hanse.shareplate.core.share.domain.Share;
import louie.hanse.shareplate.core.share.service.ShareService;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.WishExceptionType;
import louie.hanse.shareplate.core.share.repository.ShareRepository;
import louie.hanse.shareplate.core.wish.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishService {

    private final MemberService memberService;
    private final ShareService shareService;
    private final WishRepository wishRepository;
    private final ShareRepository shareRepository;

    @Transactional
    public void register(Long memberId, Long shareId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = shareService.findByIdOrElseThrow(shareId);

        share.isCanceledThrowException();
        if (isExistWish(memberId, shareId)) {
            throw new GlobalException(WishExceptionType.SHARE_ALREADY_WISH);
        }
        if (isShareWriter(memberId, shareId)) {
            throw new GlobalException(WishExceptionType.WRITER_CAN_NOT_WISH);
        }

        Wish wish = new Wish(share, member);
        wishRepository.save(wish);
    }

    @Transactional
    public void delete(Long memberId, Long shareId) {
        memberService.findByIdOrElseThrow(memberId);
        Share share = shareService.findByIdOrElseThrow(shareId);

        share.isCanceledThrowException();
        if (!isExistWish(memberId, shareId)) {
            throw new GlobalException(WishExceptionType.SHARE_NOT_WISH);
        }

        wishRepository.deleteByMemberIdAndShareId(memberId, shareId);
    }

    private boolean isExistWish(Long memberId, Long shareId) {
        return wishRepository.existsByMemberIdAndShareId(memberId, shareId);
    }

    private boolean isShareWriter(Long memberId, Long shareId) {
        return shareRepository.existsByIdAndWriterId(shareId, memberId);
    }
}
