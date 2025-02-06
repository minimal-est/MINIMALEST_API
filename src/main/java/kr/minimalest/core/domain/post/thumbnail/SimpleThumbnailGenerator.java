package kr.minimalest.core.domain.post.thumbnail;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

@Service
public class SimpleThumbnailGenerator implements ThumbnailGenerator {

    @Value("${thumbnail.width}")
    private int THUMBNAIL_WIDTH = 300;

    @Value("${thumbnail.height}")
    private int THUMBNAIL_HEIGHT = 300;

    public OutputStream create(String imageUrl, String pureExt) {
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            OutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(inputStream)
                    .size(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
                    .outputQuality(0.9)
                    .outputFormat(pureExt)
                    .toOutputStream(outputStream);

            return outputStream;
        } catch (IOException e) {
            throw new RuntimeException("썸네일 변환에 실패했습니다!", e);
        }
    }
}
