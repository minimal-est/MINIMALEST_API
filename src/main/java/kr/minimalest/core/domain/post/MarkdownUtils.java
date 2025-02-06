package kr.minimalest.core.domain.post;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public final class MarkdownUtils {

    public static <T> List<T> extract(String target, Class<T> type) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(target);

        List<Image> images = new ArrayList<>();
        List<Heading> headings = new ArrayList<>();

        document.accept(new AbstractVisitor() {
            @Override
            public void visit(Image image) {
                images.add(image);
            }

            @Override
            public void visit(Heading heading) {
                headings.add(heading);
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

        return List.of();
    }
}
