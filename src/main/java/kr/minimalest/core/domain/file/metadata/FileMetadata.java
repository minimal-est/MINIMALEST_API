package kr.minimalest.core.domain.file.metadata;

import kr.minimalest.core.domain.file.File;
import kr.minimalest.core.domain.file.StorageType;
import kr.minimalest.core.domain.post.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class FileMetadata {
    private String filename;
    private String storageKey;
    private String virtualUrl;
    private StorageType storageType;
    private Post post;

    private FileMetadata() {}

    public static FileMetadata fromEntity(File file) {
        return FileMetadata.create(
                file.getFilename(),
                file.getStorageKey(),
                file.getVirtualUrl(),
                file.getStorageType(),
                file.getPost()
        );
    }

    public static FileMetadata fromExternalReference(String key, String virtualUrl, Post post) {
        return FileMetadata.create(
                key,
                key,
                virtualUrl,
                StorageType.EXTERNAL,
                post
        );
    }

    public static FileMetadata create(String filename, String storageKey, String virtualUrl, StorageType storageType,
                                      Post post) {
        return FileMetadata.builder()
                .filename(filename)
                .storageKey(storageKey)
                .virtualUrl(virtualUrl)
                .storageType(storageType)
                .post(post)
                .build();
    }
}
