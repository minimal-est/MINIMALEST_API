package kr.minimalest.core.domain.archive;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArchiveConstants {
    public static final int AUTHOR_MIN_LENGTH = 2;
    public static final int AUTHOR_MAX_LENGTH = 15;
    public static final int MAIN_TITLE_MAX_LENGTH = 30;
    public static final int SUB_TITLE_MAX_LENGTH = 30;
}
