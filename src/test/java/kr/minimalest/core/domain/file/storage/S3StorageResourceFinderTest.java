package kr.minimalest.core.domain.file.storage;

import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3StorageResourceFinderTest {

    @Mock
    S3Operations s3Operations;

    S3StorageResourceFinder storageResourceFinder;

    @BeforeEach
    void initS3StorageResourceFinder() {
        storageResourceFinder = new S3StorageResourceFinder("sample-bucket", s3Operations);
    }

    @Test
    @DisplayName("find 중 리소스 download 성공")
    void find_success() {
        // given
        String key = "sample_key";
        S3Resource mockS3Resource = mock(S3Resource.class);
        S3StorageResource mockS3StorageResource = new S3StorageResource(mockS3Resource);

        when(s3Operations.download(anyString(), eq(key))).thenReturn(mockS3Resource);

        // when
        StorageResource resultStorageResource = storageResourceFinder.find(key);

        // then
        assertThat(resultStorageResource)
                .usingRecursiveComparison()
                .isEqualTo(mockS3StorageResource);
    }

    @Test
    @DisplayName("find 중 리소스 download 실패 예외")
    void find_downloadFail_throwsException() {
        // given
        String key = "sample_key";
        when(s3Operations.download(anyString(), eq(key))).thenThrow(RuntimeException.class);

        // when & then
        StorageResourceDownloadException ex = assertThrows(StorageResourceDownloadException.class, () -> {
            storageResourceFinder.find(key);
        });
        assertThat(ex.getMessage()).isEqualTo("S3에서 해당 key값의 리소스를 다운로드 중 실패 했습니다!");
    }

    @Test
    @DisplayName("find 중 key값이 빈값일 경우 예외 발생")
    void find_emptyKey_throwsException() {
        // given
        String key = "   ";

        // when & then
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            storageResourceFinder.find(key);
        });
        assertThat(ex.getMessage()).isEqualTo("key값이 비어있습니다!");
    }

    @Test
    @DisplayName("find 중 key값이 null 경우 예외 발생")
    void find_nullKey_throwsException() {
        // given
        String key = null;

        // when & then
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            storageResourceFinder.find(key);
        });
        assertThat(ex.getMessage()).isEqualTo("key값이 비어있습니다!");
    }
}
