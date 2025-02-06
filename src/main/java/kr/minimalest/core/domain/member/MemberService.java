package kr.minimalest.core.domain.member;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.auth.dto.LoginRequest;
import kr.minimalest.core.domain.member.dto.MemberFindResponse;
import kr.minimalest.core.domain.member.exception.MemberValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static kr.minimalest.core.domain.member.MemberUtils.encodePassword;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 닉네임 회원 조회
    public MemberFindResponse findMemberInfo(String username) {
        Optional<Member> optionalMember = memberRepository.findMemberByUsername(username);
        Member findMember = optionalMember.orElseThrow(() -> new EntityNotFoundException("해당 회원을 찾을 수 없습니다!"));
        return MemberFindResponse.builder()
                .username(findMember.getUsername())
                .email(findMember.getEmail())
                .build();
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
