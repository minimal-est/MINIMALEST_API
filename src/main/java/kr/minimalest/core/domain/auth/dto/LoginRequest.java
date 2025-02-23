package kr.minimalest.core.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String rawPassword;
}
