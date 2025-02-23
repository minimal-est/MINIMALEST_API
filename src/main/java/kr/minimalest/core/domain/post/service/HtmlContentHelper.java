package kr.minimalest.core.domain.post.service;

import kr.minimalest.core.domain.post.utils.SummaryUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.commonmark.node.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HtmlContentHelper implements ContentHelper {

    @Override
    public <T> List<T> extract(String target, Class<T> type) {
        Document document = Jsoup.parse(target);

        if (type.isAssignableFrom(Image.class)) {
            return extractImages(document).stream()
                    .map(type::cast)
                    .toList();
        } else {
            throw new IllegalStateException("추출할 수 없는 type 입니다!");
        }
    }

    @Override
    public String extractPureText(String target) {
        Document document = Jsoup.parse(target);
        return document.text();
    }

    @Override
    public String summarize(String content, int maxLength) {
        return SummaryUtils.summarize(extractPureText(content), maxLength);
    }

    private List<Image> extractImages(Document doc) {
        List<Image> images = new ArrayList<>();
        Elements imgElements = doc.select("img");

        for (Element imgElement : imgElements) {
            String src = imgElement.attr("src");
            Image image = new Image();
            image.setDestination(src);
            images.add(image);
        }

        return images;
    }
}
