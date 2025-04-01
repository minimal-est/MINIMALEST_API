package kr.minimalest.core.domain.post.service;

import java.io.OutputStream;

public interface ThumbnailGenerator {

    void create(String imageUrl, String pureExt, OutputStream outputStream);
}
