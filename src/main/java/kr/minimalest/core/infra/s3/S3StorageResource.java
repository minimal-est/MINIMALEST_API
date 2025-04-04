package kr.minimalest.core.infra.s3;

import io.awspring.cloud.s3.S3Resource;
import kr.minimalest.core.domain.file.StorageResource;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class S3StorageResource implements StorageResource {

    private final S3Resource s3Resource;

    @Override
    public InputStream getInputStream() {
        try {
            return s3Resource.getInputStream();
        } catch (IOException e) {
            throw new IllegalStateException("S3 리소스의 InputStream을 가져오는 데 실패했습니다!");
        }
    }

    @Override
    public String getContentType() {
        return s3Resource.contentType();
    }
}
