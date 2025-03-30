package kr.minimalest.core.domain.member;

import kr.minimalest.core.domain.auth.AuthType;
import kr.minimalest.core.domain.member.dto.MemberJoinRequest;
import kr.minimalest.core.domain.member.dto.MemberJoinResponse;
import kr.minimalest.core.domain.profile.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberApiTest {
    @Mock
    private MemberRepository memberRepository;  // Mock 객체 생성

    @InjectMocks
    private MemberService memberService;  // Mock 객체를 주입

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .email("test@example.com")
                .username("testUser")
                .encPassword("encodedPassword")
                .profile(new Profile("profile.jpg"))
                .build();
    }

    @Test
    void 회원가입_성공() {
        // given
        MemberJoinRequest request = new MemberJoinRequest("testUser", "testUser", "test@example.com", "profile.jpg",
                AuthType.JWT);
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        // when
        MemberJoinResponse response = memberService.joinMember(request);

        // then
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        verify(memberRepository, times(1)).save(any(Member.class));
    }
}
