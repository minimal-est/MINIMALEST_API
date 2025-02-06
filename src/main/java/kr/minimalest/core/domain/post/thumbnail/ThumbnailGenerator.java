package kr.minimalest.core.domain.post.thumbnail;

import java.io.OutputStream;

public interface ThumbnailGenerator {

    OutputStream create(String imageUrl, String pureExt);
}
