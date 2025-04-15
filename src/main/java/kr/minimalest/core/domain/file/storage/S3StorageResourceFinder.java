package kr.minimalest.core.domain.file.storage;

import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3StorageResourceFinder implements StorageResourceFinder {

    @Value("${spring.cloud.aws.bucket.name}")
    private String BUCKET;

    private final S3Operations s3Operations;

    @Override
    public StorageResource find(String key) {
        S3Resource s3Resource = s3Operations.download(BUCKET, key);
        return new S3StorageResource(s3Resource);
    }
}
