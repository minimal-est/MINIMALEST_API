package kr.minimalest.core.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegexUtils {
    public static final String NO_SPECIAL_CHAR_PATTERN = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]+$";
}
