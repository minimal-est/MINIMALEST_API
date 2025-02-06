package kr.minimalest.core.domain.member;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MemberUtils {

    // SHA-256 알고리즘
    public static String encodePassword(String rawPassword) {
        try {
            // 해시 알고리즘 생성
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(rawPassword.getBytes());

            // 바이트 배열 문자열(16진수)로 변환
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("해당 Password 인코딩 알고리즘이 존재하지 않습니다.", e);
        }
    }
}
