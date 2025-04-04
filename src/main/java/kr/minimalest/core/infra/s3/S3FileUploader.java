package kr.minimalest.core.infra.s3;

import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import kr.minimalest.core.domain.file.*;
import kr.minimalest.core.domain.file.exception.FileUploaderException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3FileUploader implements FileUploader {

    private final S3Operations s3Operations;

    @Value("${spring.cloud.aws.bucket.name}")
    private String BUCKET;

    @Override
    public FileStorageType getStorageType() {
        return FileStorageType.S3;
    }

    @Override
    public StorageResource findResource(String key) {
        S3Resource resource = s3Operations.download(BUCKET, key);

        if (!resource.exists()) {
            throw new IllegalArgumentException("해당 key의 리소스는 존재하지 않습니다!");
        }

        return new S3StorageResource(resource);
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        String key = FileUtils.createKey(multipartFile);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            s3Operations.upload(BUCKET, key, inputStream);
        } catch (IOException e) {
            throw new FileUploaderException("S3 파일 업로드에 실패했습니다!", e);
        }

        return key;
    }

    @Override
    public String uploadFile(InputStream inputStream, String filename, String contentType) {
        String key = FileUtils.createKey(filename, contentType);

        s3Operations.upload(BUCKET, key, inputStream);

        return key;
    }
}
