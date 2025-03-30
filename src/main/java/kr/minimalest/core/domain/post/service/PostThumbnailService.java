package kr.minimalest.core.domain.post.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.minimalest.core.common.utils.ServerHostResolver;
import kr.minimalest.core.domain.file.FileService;
import kr.minimalest.core.domain.file.FileUtils;
import kr.minimalest.core.domain.file.dto.FileResponse;
import kr.minimalest.core.domain.post.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;

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

        // 선정한 이미지 압축
        OutputStream thumbnailOutputStream = thumbnailGenerator.create(thumbnailUrl, pureExt);

        // 썸네일 저장
        return fileService.uploadAndSave(post, thumbnailOutputStream, filename, contentType);
    }
}
