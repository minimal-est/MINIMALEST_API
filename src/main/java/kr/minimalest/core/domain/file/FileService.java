package kr.minimalest.core.domain.file;

import kr.minimalest.core.domain.file.dto.FileResponse;
import kr.minimalest.core.domain.file.metadata.FileMetadata;
import kr.minimalest.core.domain.file.metadata.FileMetadataSaver;
import kr.minimalest.core.domain.file.storage.MultipartUploadFile;
import kr.minimalest.core.domain.file.storage.StorageResource;
import kr.minimalest.core.domain.file.storage.StorageResourceFinder;
import kr.minimalest.core.domain.file.storage.StorageResourceUploader;
import kr.minimalest.core.domain.post.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileMetadataSaver fileMetadataSaver;
    private final StorageResourceFinder storageResourceFinder;
    private final StorageResourceUploader storageResourceUploader;
    private final FileVirtualUrlResolver fileVirtualUrlResolver;

    public StorageResource findStorageResource(String key) {
        return storageResourceFinder.find(key);
    }

    public FileResponse save(Post post, String key) {
        return saveFileMetadataAndGetFileResponse(key, key, post);
    }

    // 파일 업로드 및 저장 메서드 (Multipart)
    public FileResponse uploadAndSave(Post post, MultipartFile multipartFile) {
        // 파일 스토리지에 업로드
        String key = storageResourceUploader.uploadAndGetKey(new MultipartUploadFile(multipartFile));
        return saveFileMetadataAndGetFileResponse(multipartFile.getOriginalFilename(), key, post);
    }

    private FileResponse saveFileMetadataAndGetFileResponse(String filename, String key, Post post) {
        // 가상 주소 및 저장소 타입 얻기
        String virtualUrl = fileVirtualUrlResolver.resolve(key);
        StorageType storageType = storageResourceUploader.getStorageType();

        // DB에 저장
        FileMetadata fileMetadata = FileMetadata.create(filename, key, virtualUrl, storageType, post);
        fileMetadataSaver.saveFileMetadata(fileMetadata);

        return new FileResponse(filename, virtualUrl, key);
    }
}
