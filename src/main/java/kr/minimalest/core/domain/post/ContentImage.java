package kr.minimalest.core.domain.post;

import kr.minimalest.core.domain.file.FileUtils;
import kr.minimalest.core.domain.post.service.Extractor;
import org.commonmark.node.Image;

import java.util.List;

public class ContentImage {

    private final String url;

    public ContentImage(Image image) {
        this.url = image.getDestination();
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isContained(List<String> savedUrls) {
        return savedUrls.contains(url);
    }

    public String resolveKey() {
        String pureExt = FileUtils.extractExt(url);
        String contentType = "image/" + pureExt;
        String type = FileUtils.extractType(contentType);
        return FileUtils.keyResolver(type, pureExt);
    }

    public static List<ContentImage> extractContentImages(String content, Extractor extractor) {
        List<Image> images = extractor.extract(content, Image.class);
        return images.stream()
                .map(ContentImage::new)
                .toList();
    }
}
