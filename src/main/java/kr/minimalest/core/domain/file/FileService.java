package kr.minimalest.core.domain.file;

import kr.minimalest.core.domain.file.dto.FileResponse;
import kr.minimalest.core.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileUploader fileUploader;
    private final FileStorageService fileStorageService;

    @Value("${file.proxy.url}")
    private String VIRTUAL_REQUEST;

    public StorageResource findResource(String key) {
        return fileUploader.findResource(key);
    }

    // 파일 업로드 및 저장 메서드 (OutputStream)
    public FileResponse uploadAndSave(@Nullable Post post, OutputStream outputStream, String filename, String contentType) {
        // 파일 스토리지에 업로드
        String key = fileUploader.uploadFile(toInputStream(outputStream), filename, contentType);

        FileStorageType storageType = fileUploader.getStorageType();
        String virtualUrl = virtualUrlResolver(key, VIRTUAL_REQUEST);

        // 파일 메타데이터 DB에 저장
        fileStorageService.saveMetadata(filename, key, virtualUrl, storageType, post);

        return new FileResponse(filename, virtualUrl, key);
    }

    // 파일 업로드 및 저장 메서드 (Multipart)
    public FileResponse uploadAndSave(@Nullable Post post, MultipartFile multipartFile) {
        // 파일 스토리지에 업로드
        String key = fileUploader.uploadFile(multipartFile);

        FileStorageType storageType = fileUploader.getStorageType();
        String virtualUrl = virtualUrlResolver(key, VIRTUAL_REQUEST);

        // 파일 메타데이터 DB에 저장
        fileStorageService.saveMetadata(multipartFile, key, virtualUrl, storageType, post);

        return new FileResponse(multipartFile.getOriginalFilename(), virtualUrl, key);
    }

    private static String virtualUrlResolver(String key, String virtualUrl) {
        return virtualUrl + "?key=" + key;
    }

    private static InputStream toInputStream(OutputStream outputStream) {
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;

        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return new ByteArrayInputStream(byteArray);
    }
}
