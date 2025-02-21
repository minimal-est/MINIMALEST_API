package kr.minimalest.core.domain.member.dto;

import kr.minimalest.core.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberFindResponse {

    private String username;
    private String email;

    public static MemberFindResponse fromEntity(Member member) {
        return MemberFindResponse.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .build();
    }
}
