package kr.minimalest.core.domain.post.service;

import kr.minimalest.core.domain.file.FileService;
import kr.minimalest.core.domain.file.FileUtils;
import kr.minimalest.core.domain.file.dto.FileResponse;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.ThumbnailGenerator;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Image;
import org.springframework.stereotype.Service;

import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class PostThumbnailService {

    private final ThumbnailGenerator thumbnailGenerator;
    private final FileService fileService;

    public FileResponse createAndUploadAndSaveThumbnail(Image image, Post post) {
        // 썸네일로 사용될 이미지 정보
        String thumbnailUrl = image.getDestination();
        String filename = FileUtils.extractKeyParameter(thumbnailUrl);
        String pureExt = FileUtils.extractPureExt(filename);
        String contentType = FileUtils.toImageContentType(pureExt);

        // 선정한 이미지 압축
        OutputStream thumbnailOutputStream = thumbnailGenerator.create(thumbnailUrl, pureExt);

        // 썸네일 저장
        return fileService.uploadAndSave(post, thumbnailOutputStream, filename, contentType);
    }
}
