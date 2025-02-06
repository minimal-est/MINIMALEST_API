package kr.minimalest.core.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberJoinRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String rawPassword;

    @NotBlank
    private String email;
}
