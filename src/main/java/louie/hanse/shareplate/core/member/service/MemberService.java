package louie.hanse.shareplate.core.member.service;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.MemberExceptionType;
import louie.hanse.shareplate.core.member.repository.MemberRepository;
import louie.hanse.shareplate.common.uploader.FileUploader;
import louie.hanse.shareplate.core.member.dto.request.MemberChangeUserInfoRequest;
import louie.hanse.shareplate.core.member.dto.response.MemberUserInfoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final FileUploader fileUploader;

    @Transactional
    public void changeUserInfo(MemberChangeUserInfoRequest request, Long id) throws IOException {
        Member member = findByIdOrElseThrow(id);

        MultipartFile image = request.getImage();
        if (!ObjectUtils.isEmpty(image)) {
            String imageUrl = fileUploader.uploadMemberImage(image);
            member.changeProfileImageUrl(imageUrl);
            member.changeThumbnailImageUrl(imageUrl);
        }

        String nickname = request.getNickname();
        if (StringUtils.hasText(nickname)) {
            member.changeNickname(nickname);
        }
    }

    public MemberUserInfoResponse getUserInfo(Long id) {
        Member member = findByIdOrElseThrow(id);
        return new MemberUserInfoResponse(member);
    }

    public Member findByIdOrElseThrow(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new GlobalException(MemberExceptionType.MEMBER_NOT_FOUND));
    }
}
