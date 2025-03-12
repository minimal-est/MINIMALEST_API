package kr.minimalest.core.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.minimalest.core.domain.member.Member;
import kr.minimalest.core.domain.member.MemberUtils;
import kr.minimalest.core.domain.member.UserLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
public class MemberJoinRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String rawPassword;

    @Email
    @NotBlank
    private String email;

    @URL
    private String profileImageUrl;

    public static Member toEntity(MemberJoinRequest memberJoinRequest) {
        return Member.builder()
                .username(memberJoinRequest.getUsername())
                .encPassword(MemberUtils.encodePassword(memberJoinRequest.getRawPassword()))
                .email(memberJoinRequest.getEmail())
                .userLevel(UserLevel.MEMBER)
                .build();
    }
}
