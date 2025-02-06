package kr.minimalest.core.domain.file;

import java.io.InputStream;

public interface StorageResource {

    InputStream getInputStream();

    String getContentType();
}
