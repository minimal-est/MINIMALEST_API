package kr.minimalest.core.domain.post;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.commonmark.node.Text;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SummaryUtils {

    public static final String enderSb = "...";

    public static String createContentSummary(String content, int maxLength) {
        List<Text> texts = MarkdownUtils.extract(content, Text.class);
        StringBuilder summarySb = new StringBuilder();

        for (Text text : texts) {
            String literal = text.getLiteral();
            if (!summarySb.isEmpty()) summarySb.append(" ");
            if (summarySb.length() + literal.length() <= maxLength) {
                summarySb.append(literal);
            } else {
                summarySb.append(literal, 0, maxLength - summarySb.length());
                break;
            }
        }

        if (content.length() > maxLength) summarySb.append(enderSb);

        return summarySb.toString();
    }

    public static String createTitleSummary(String title, int maxLength) {
        StringBuilder titleSb = new StringBuilder();

        if (title.length() > maxLength) {
            titleSb.append(title, 0, maxLength);
            titleSb.append(enderSb);
        } else {
            titleSb.append(title);
        }

        return titleSb.toString();
    }
}
