package kr.minimalest.core.domain.file.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
public class MultipartUploadFile implements UploadFile {
    private final MultipartFile multipartFile;

    @Override
    public InputStream getInputStream() throws IOException {
        return multipartFile.getInputStream();
    }

    @Override
    public String getOriginalFilename() {
        return multipartFile.getOriginalFilename();
    }

    @Override
    public long getSize() {
        return multipartFile.getSize();
    }

    @Override
    public String getContentType() {
        return multipartFile.getContentType();
    }
}
