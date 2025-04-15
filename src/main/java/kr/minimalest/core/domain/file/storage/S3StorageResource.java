package kr.minimalest.core.domain.file.storage;

import io.awspring.cloud.s3.S3Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
public class S3StorageResource implements StorageResource {

    private final S3Resource s3Resource;

    @Override
    public InputStream getInputStream() {
        try {
            return s3Resource.getInputStream();
        } catch (IOException e) {
            log.error("S3에서 InputStream을 얻는 데 실패했습니다! location: {}", s3Resource.getLocation());
            throw new IllegalStateException("S3 리소스의 InputStream을 가져오는 데 실패했습니다!", e);
        }
    }

    @Override
    public String getContentType() {
        return s3Resource.contentType();
    }
}
