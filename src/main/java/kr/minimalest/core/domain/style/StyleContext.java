package kr.minimalest.core.domain.style;

import kr.minimalest.core.domain.style.type.StyleKeyType;

import java.util.Set;

import static kr.minimalest.core.domain.style.type.StyleKeyType.*;

/**
 * 스타일에 적용할 수 있는 속성을 관리합니다. 기본값을 지정하려면, Style 엔티티의 defaultFor**메소드를 확인하세요.
 * @see kr.minimalest.core.domain.style.entity.Style
 */
public enum StyleContext {
    /**
     * 아카이브 스타일에 적용할 수 있는 속성입니다.
     */
    ARCHIVE(Set.of(
            BACKGROUND_COLOR,
            FONT_COLOR
    )),

    /**
     * 포스트 스타일에 적용할 수 있는 속성입니다.
     */
    POST(Set.of(
            BACKGROUND_COLOR,
            FONT_COLOR
    ));

    private final Set<StyleKeyType> acceptableKeys;

    StyleContext(Set<StyleKeyType> acceptableKeys) {
        this.acceptableKeys = acceptableKeys;
    }

    public boolean isAcceptable(StyleKeyType key) {
        return acceptableKeys.contains(key);
    }
}
