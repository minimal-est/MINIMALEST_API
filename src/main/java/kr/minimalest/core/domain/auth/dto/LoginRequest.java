package kr.minimalest.core.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.minimalest.core.domain.auth.AuthType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    @Email
    private String email;

    private String rawPassword;

    @NotNull
    private AuthType authType;;
}
