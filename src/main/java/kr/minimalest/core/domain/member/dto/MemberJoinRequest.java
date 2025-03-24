package kr.minimalest.core.domain.member.dto;

import jakarta.validation.constraints.*;
import kr.minimalest.core.common.utils.RegexUtils;
import kr.minimalest.core.domain.auth.AuthType;
import kr.minimalest.core.domain.member.Member;
import kr.minimalest.core.domain.member.MemberUtils;
import kr.minimalest.core.domain.member.UserLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import static kr.minimalest.core.domain.member.MemberConstants.*;

@Data
@AllArgsConstructor
public class MemberJoinRequest {

    @NotBlank
    @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH)
    @Pattern(
            regexp = RegexUtils.NO_SPECIAL_CHAR_PATTERN,
            message = "대표이름은 특수문자와 공백이 포함될 수 없습니다."
    )
    private String username;

    @NotBlank
    private String rawPassword;

    @Email
    @NotBlank
    private String email;

    private String profileImageUrl;

    @NotNull
    private AuthType authType;

    public static Member toEntity(MemberJoinRequest memberJoinRequest) {
        return Member.builder()
                .username(memberJoinRequest.getUsername())
                .encPassword(MemberUtils.encodePassword(memberJoinRequest.getRawPassword()))
                .email(memberJoinRequest.getEmail())
                .userLevel(UserLevel.MEMBER)
                .authType(memberJoinRequest.authType)
                .build();
    }
}
