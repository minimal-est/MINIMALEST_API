package kr.minimalest.core.domain.auth;

public enum AuthType {
    JWT("jwt"), // 순수 JWT
    GOOGLE("google"); // 구글 OAuth

    AuthType(String type) {}
}
