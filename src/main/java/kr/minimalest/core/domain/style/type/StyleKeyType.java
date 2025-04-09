package kr.minimalest.core.domain.style.type;

public enum StyleKeyType {
    BACKGROUND_COLOR("backgroundColor"),
    FONT_COLOR("fontColor"),
    TITLE_COLOR("titleColor");

    private final String value;

    StyleKeyType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
