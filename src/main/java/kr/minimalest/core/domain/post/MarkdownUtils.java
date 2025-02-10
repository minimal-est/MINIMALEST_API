package kr.minimalest.core.domain.post;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MarkdownUtils {

    public static <T> List<T> extract(String target, Class<T> type) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(target);

        List<Image> images = new ArrayList<>();
        List<Heading> headings = new ArrayList<>();
        List<Text> texts = new ArrayList<>();

        document.accept(new AbstractVisitor() {
            @Override
            public void visit(Image image) {
                images.add(image);
            }

            @Override
            public void visit(Heading heading) {
                headings.add(heading);
            }

            @Override
            public void visit(Text text) {
                texts.add(text);
            }
        });

        if (type.isAssignableFrom(Image.class)) {
            return images.stream()
                    .map(type::cast)
                    .toList();
        }

        if (type.isAssignableFrom(Heading.class)) {
            return headings.stream()
                    .map(type::cast)
                    .toList();
        }

        if (type.isAssignableFrom(Text.class)) {
            return texts.stream()
                    .map(type::cast)
                    .toList();
        }

        return List.of();
    }
}
