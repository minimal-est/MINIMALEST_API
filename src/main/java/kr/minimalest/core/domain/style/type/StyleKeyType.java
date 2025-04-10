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

    public static StyleKeyType fromValue(String value) {
        for (StyleKeyType type : StyleKeyType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("해당하는 key값이 없습니다!: " + value);
    }
}
