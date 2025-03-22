package kr.minimalest.core.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.minimalest.core.domain.auth.AuthType;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    @Email
    private String email;

    private String rawPassword;

    @NotBlank
    private AuthType authType;;
}
