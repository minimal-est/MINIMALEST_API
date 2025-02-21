package kr.minimalest.core.domain.member;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.auth.dto.LoginRequest;
import kr.minimalest.core.domain.member.dto.MemberFindResponse;
import kr.minimalest.core.domain.member.exception.MemberValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static kr.minimalest.core.domain.member.MemberUtils.encodePassword;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberFindResponse findMemberInfo(String email, String author) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElseThrow(() -> new EntityNotFoundException("해당 회원을 찾을 수 없습니다!"));
        for (Archive archive : member.getArchives()) {
            log.info(archive.getAuthor());
        }
        boolean isExists = member.getArchives().stream().anyMatch((archive) -> archive.getAuthor().equals(author));
        if (!isExists) {
            throw new IllegalArgumentException("해당 author의 아카이브가 존재하지 않습니다!");
        }
        return MemberFindResponse.fromEntity(member);
    }

    public MemberFindResponse findMemberInfo(String username) {
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        Member member = optionalMember.orElseThrow(() -> new EntityNotFoundException("해당 회원을 찾을 수 없습니다!"));
        return MemberFindResponse.fromEntity(member);
    }

    // 회원 검증
    public void validateLogin(LoginRequest loginRequest) {
        boolean validation = memberRepository.existsByEmailAndEncPassword(
                loginRequest.getEmail(),
                encodePassword(loginRequest.getRawPassword())
        );

        if (!validation) {
            throw new MemberValidationException("유효한 회원이 아닙니다!");
        }
    }
}
