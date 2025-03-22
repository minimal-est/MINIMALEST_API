package kr.minimalest.core.domain.auth;

import kr.minimalest.core.domain.auth.dto.LoginRequest;
import kr.minimalest.core.domain.auth.dto.LoginSuccessResponse;

public interface AuthStrategy {

    LoginSuccessResponse login(LoginRequest loginRequest);
}
