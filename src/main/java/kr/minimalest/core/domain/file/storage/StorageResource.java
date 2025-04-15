package kr.minimalest.core.domain.file.storage;

import java.io.InputStream;

public interface StorageResource {

    InputStream getInputStream();

    String getContentType();
}
