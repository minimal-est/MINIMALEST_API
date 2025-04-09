package kr.minimalest.core.domain.style;

import kr.minimalest.core.domain.style.type.StyleKeyType;

import java.util.Set;

import static kr.minimalest.core.domain.style.type.StyleKeyType.*;

public enum StyleContext {
    ARCHIVE(Set.of(
            BACKGROUND_COLOR,
            FONT_COLOR
    )),

    POST(Set.of(
            BACKGROUND_COLOR
    ));

    private final Set<StyleKeyType> acceptableKeys;

    StyleContext(Set<StyleKeyType> acceptableKeys) {
        this.acceptableKeys = acceptableKeys;
    }

    public boolean isAcceptable(StyleKeyType key) {
        return acceptableKeys.contains(key);
    }
}
