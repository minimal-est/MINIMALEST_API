package kr.minimalest.core.domain.file.storage;

import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class S3StorageResourceFinder implements StorageResourceFinder {

    private final String bucketName;
    private final S3Operations s3Operations;

    @Override
    public StorageResource find(String key) {
        hasKey(key);
        S3Resource resource = download(key);
        return new S3StorageResource(resource);
    }

    private static void hasKey(String key) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalStateException("key값이 비어있습니다!");
        }
    }

    private S3Resource download(String key) {
        try {
            return s3Operations.download(bucketName, key);
        } catch (Exception ex) {
            log.error("S3에서 해당 key값의 리소스를 다운로드 중 실패 했습니다! key: {}", key, ex);
            throw new StorageResourceDownloadException("S3에서 해당 key값의 리소스를 다운로드 중 실패 했습니다!", ex);
        }
    }
}
