package kr.minimalest.core.domain.post.utils;

import org.springframework.util.StringUtils;

public class SummaryUtils {

    public static String summarize(String target, int maxLength) {
        if (!StringUtils.hasText(target)) return "";

        StringBuilder summary = new StringBuilder(target);

        if (summary.length() > maxLength) {
            summary.setLength(maxLength);
            summary.append(" (..)");
        }

        return summary.toString();
    }
}
