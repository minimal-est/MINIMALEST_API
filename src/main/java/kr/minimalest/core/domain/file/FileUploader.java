package kr.minimalest.core.domain.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileUploader {

    FileStorageType getStorageType();

    StorageResource findResource(String key);

    String uploadFile(MultipartFile multipartFile);

    String uploadFile(InputStream inputStream, String filename, String contentType);
}
