package kr.minimalest.core.domain.file;

import kr.minimalest.core.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileRepository fileRepository;

    public void saveMetadataFromExternal(
            String key,
            String url,
            @Nullable Post post
    ) {
        File file = File.builder()
                .filename(key)
                .fileStorageType(FileStorageType.EXTERNAL)
                .virtualUrl(url)
                .post(post)
                .storageKey(key)
                .build();

        fileRepository.save(file);
    }

    public void saveMetadata(
            MultipartFile multipartFile,
            String storageKey,
            String virtualUrl,
            FileStorageType fileStorageType,
            @Nullable Post post
    ) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다!");
        }

        File file = File.builder()
                .filename(multipartFile.getOriginalFilename())
                .storageKey(storageKey)
                .virtualUrl(virtualUrl)
                .fileStorageType(fileStorageType)
                .post(post)
                .build();

        // DB에 저장
        fileRepository.save(file);
    }

    public void saveMetadata(
            String filename,
            String storageKey,
            String virtualUrl,
            FileStorageType fileStorageType,
            @Nullable Post post
    ) {
        File file = File.builder()
                .filename(filename)
                .storageKey(storageKey)
                .virtualUrl(virtualUrl)
                .fileStorageType(fileStorageType)
                .post(post)
                .build();

        // DB에 저장
        fileRepository.save(file);
    }
}
