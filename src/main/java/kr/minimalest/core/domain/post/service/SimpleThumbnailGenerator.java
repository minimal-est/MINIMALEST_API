package kr.minimalest.core.domain.post.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import static kr.minimalest.core.domain.post.utils.PostConstants.MAX_THUMBNAIL_HEIGHT;
import static kr.minimalest.core.domain.post.utils.PostConstants.MAX_THUMBNAIL_WIDTH;

@Service
public class SimpleThumbnailGenerator implements ThumbnailGenerator {

    @Override
    public OutputStream create(String imageUrl, String pureExt) {
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            OutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(inputStream)
                    .size(MAX_THUMBNAIL_WIDTH, MAX_THUMBNAIL_HEIGHT)
                    .outputQuality(1.0f)
                    .outputFormat(pureExt)
                    .toOutputStream(outputStream);

            return outputStream;
        } catch (IOException e) {
            throw new RuntimeException("썸네일 변환에 실패했습니다!", e);
        }
    }
}
