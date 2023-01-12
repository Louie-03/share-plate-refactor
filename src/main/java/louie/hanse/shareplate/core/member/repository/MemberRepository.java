package louie.hanse.shareplate.core.member.repository;

import louie.hanse.shareplate.core.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
