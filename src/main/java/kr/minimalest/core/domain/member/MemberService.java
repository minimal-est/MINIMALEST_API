package kr.minimalest.core.domain.member;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.auth.AuthType;
import kr.minimalest.core.domain.auth.dto.LoginRequest;
import kr.minimalest.core.domain.member.dto.MemberFindResponse;
import kr.minimalest.core.domain.member.dto.MemberJoinRequest;
import kr.minimalest.core.domain.member.dto.MemberJoinResponse;
import kr.minimalest.core.domain.member.exception.MemberConflictException;
import kr.minimalest.core.domain.member.exception.MemberValidationException;
import kr.minimalest.core.domain.profile.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MemberJoinResponse joinMember(MemberJoinRequest memberJoinRequest) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberJoinRequest.getEmail());
        if (optionalMember.isPresent()) throw new MemberConflictException("이미 존재하는 회원입니다!");

        Member member = MemberJoinRequest.toEntity(memberJoinRequest);
        Profile profile = new Profile(memberJoinRequest.getProfileImageUrl());
        member.setProfile(profile);
        memberRepository.save(member);
        return new MemberJoinResponse(member.getEmail());
    }

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
        boolean validation = false;

        if (loginRequest.getAuthType() == AuthType.JWT) {
            // 순수 로그인 이라면 비밀번호를 확인합니다.
            validation = memberRepository.existsByEmailAndEncPassword(
                    loginRequest.getEmail(),
                    MemberUtils.encodePassword(loginRequest.getRawPassword())
            );
        } else {
            // 다른 방법일 경우, 이메일로 검증합니다.
            validation = memberRepository.existsByEmailAndAuthType(
                    loginRequest.getEmail(),
                    loginRequest.getAuthType()
            );
        }

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
