package kr.minimalest.core.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginSuccessResponse {

    private String email;

    private boolean isNew;
}
