package kr.minimalest.core.domain.file.metadata;

import kr.minimalest.core.domain.file.File;
import kr.minimalest.core.domain.file.FileRepository;
import kr.minimalest.core.domain.file.StorageType;
import kr.minimalest.core.domain.post.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.Metadata;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileMetadataSaver {

    private final FileRepository fileRepository;

    public void saveFileMetadataFromExternalReference(String key, String url, @Nullable Post post) {
        saveFileMetadata(FileMetadata.fromExternalReference(key, url, post));
    }

    public void saveFileMetadata(FileMetadata fileMetadata) {
        saveFileMetadataWithThrows(fileMetadata);
    }

    private void saveFileMetadataWithThrows(FileMetadata fileMetadata) {
        File file = File.builder()
                .filename(fileMetadata.getFilename())
                .storageKey(fileMetadata.getStorageKey())
                .virtualUrl(fileMetadata.getVirtualUrl())
                .storageType(fileMetadata.getStorageType())
                .post(fileMetadata.getPost())
                .build();
        try {
            fileRepository.save(file);
        } catch (Exception ex) {
            log.error("FileMetadata 저장에 실패했습니다! fileMetadata: {}", fileMetadata, ex);
            throw new FileMetadataSaveException("FileMetadata 저장에 실패했습니다!", ex);
        }
    }
}
