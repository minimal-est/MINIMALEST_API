package kr.minimalest.core.domain.auth.dto;

import lombok.Data;

@Data
public class AuthStatusResponse {
    private String email;
    private String author;
    private boolean authenticated;
}
