package kr.minimalest.core.domain.file.storage;

import java.io.IOException;
import java.io.InputStream;

public interface UploadFile {

    InputStream getInputStream() throws IOException;

    String getOriginalFilename();

    long getSize();

    String getContentType();
}
