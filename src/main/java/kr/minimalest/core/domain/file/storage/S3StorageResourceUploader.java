package kr.minimalest.core.domain.file.storage;

import io.awspring.cloud.s3.S3Operations;
import kr.minimalest.core.domain.file.FileUtils;
import kr.minimalest.core.domain.file.StorageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3StorageResourceUploader implements StorageResourceUploader {

    @Value("${spring.cloud.aws.bucket.name}")
    private String BUCKET;

    private final S3Operations s3Operations;

    @Override
    public String uploadAndGetKey(UploadFile uploadFile) {
        String key = createKey(uploadFile);
        uploadToS3Storage(key, uploadFile);
        return key;
    }

    private static String createKey(UploadFile uploadFile) {
        return FileUtils.createKey(uploadFile.getOriginalFilename(), uploadFile.getContentType());
    }

    private void uploadToS3Storage(String key, UploadFile uploadFile) {
        try {
            s3Operations.upload(BUCKET, key, uploadFile.getInputStream());
        } catch (IOException ex) {
            log.error("S3에 업로드하는데 실패 했습니다. uploadFileName: {}", uploadFile.getOriginalFilename(), ex);
            throw new StorageResourceUploadException("S3에 업로드하는데 실패 했습니다.");
        }
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.S3;
    }
}
