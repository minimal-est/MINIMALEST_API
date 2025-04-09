package kr.minimalest.core.domain.style;

import kr.minimalest.core.domain.style.type.StyleKeyType;

public interface Stylable {

    void putStyle(StyleKeyType styleKeyType, String value);

    StyleContext context();
}
