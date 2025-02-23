package kr.minimalest.core.domain.member;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.auth.dto.LoginRequest;
import kr.minimalest.core.domain.member.dto.MemberFindResponse;
import kr.minimalest.core.domain.member.exception.MemberValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberFindResponse findAndValidate(String email, String author) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원을 찾을 수 없습니다!"));

        validateAuthorExists(member, author);

        return MemberFindResponse.fromEntity(member);
    }

    @Transactional(readOnly = true)
    public MemberFindResponse findAndValidate(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원을 찾을 수 없습니다!"));
        return MemberFindResponse.fromEntity(member);
    }

    @Transactional(readOnly = true)
    public void validateLogin(LoginRequest loginRequest) {
        boolean validation = memberRepository.existsByEmailAndEncPassword(
                loginRequest.getEmail(),
                MemberUtils.encodePassword(loginRequest.getRawPassword())
        );

        if (!validation) {
            throw new MemberValidationException("유효한 회원이 아닙니다!");
        }
    }

    private void validateAuthorExists(Member member, String author) {
        boolean isExists = member.getArchives().stream()
                .anyMatch((archive) -> archive.getAuthor().equals(author));

        if (!isExists) {
            throw new EntityNotFoundException("해당 회원 author의 아카이브가 존재하지 않습니다!");
        }
    }
}
