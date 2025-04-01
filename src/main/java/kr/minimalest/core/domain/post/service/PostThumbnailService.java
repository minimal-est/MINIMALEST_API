package kr.minimalest.core.domain.post.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.minimalest.core.common.utils.ServerHostResolver;
import kr.minimalest.core.domain.file.FileService;
import kr.minimalest.core.domain.file.FileUtils;
import kr.minimalest.core.domain.file.dto.FileResponse;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.exception.PostException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostThumbnailService {

    @Value("${file.proxy.url}")
    private String PROXY_URL;

    private final ThumbnailGenerator thumbnailGenerator;
    private final FileService fileService;
    private final HttpServletRequest request;

    public String createThumbnailUrl(Image image, Post post) {
        FileResponse thumbnailFile = createAndUploadAndSaveThumbnail(image, post);
        return thumbnailFile.getVirtualUrl();
    }

    private FileResponse createAndUploadAndSaveThumbnail(Image image, Post post) {
        // 썸네일로 사용될 이미지 정보
        String serverHost = ServerHostResolver.getServerHost(request);

        String thumbnailUrl = image.getDestination();

        if (image.getDestination().startsWith(PROXY_URL)) {
            // 스키마가 존재하지 않는 이미지라면
            thumbnailUrl = serverHost + image.getDestination();
        }

        String filename = FileUtils.extractKeyParameter(thumbnailUrl);
        String pureExt = FileUtils.extractPureExt(filename);
        String contentType = FileUtils.toImageContentType(pureExt);

        File file = null;
        try {
            // 임시 파일 생성
            file = File.createTempFile("minimalest", "thumbnail");

            // 썸네일 생성 후 임시파일에 쓰기
            try (FileOutputStream fos = new FileOutputStream(file)) {
                thumbnailGenerator.create(thumbnailUrl, pureExt, fos);
            }

            // 임시파일을 스토리지(S3)에 업로드 시키기
            try (FileInputStream fis = new FileInputStream(file)) {
                return fileService.uploadAndSave(post, fis, filename, contentType);
            }
        } catch (IOException ex) {
            throw new RuntimeException("임시 파일을 생성할 수 없습니다!", ex);
        } catch (RuntimeException ex) {
            throw new PostException("썸네일을 생성하는 과정에서 실패했습니다.", ex);
        } finally {
            // 임시 파일 삭제시켜주기
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }
}
