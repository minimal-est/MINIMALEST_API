package kr.minimalest.core.domain.post.service;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.ArchiveRepository;
import kr.minimalest.core.domain.archive.ArchiveService;
import kr.minimalest.core.domain.archive.dto.ArchiveInfoResponse;
import kr.minimalest.core.domain.file.dto.FileResponse;
import kr.minimalest.core.domain.post.MarkdownUtils;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.PostRepository;
import kr.minimalest.core.domain.post.dto.PostCreateRequest;
import kr.minimalest.core.domain.post.dto.PostCreateResponse;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Image;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostCreateService {

    private final PostRepository postRepository;
    private final ArchiveRepository archiveRepository;
    private final ArchiveService archiveService;
    private final PostImageService postImageService;
    private final PostThumbnailService postThumbnailService;

    @Transactional
    public PostCreateResponse create(String author, String email, PostCreateRequest request) {
        // 아카이브 검증
        ArchiveInfoResponse archiveInfoResponse = archiveService.validateArchive(author, email);

        // 아카이브의 Post Sequence 더하기
        long nextSequence = postRepository.findMaxSequenceByArchive(author) + 1;

        // 이미지 추출 후 썸네일 선정
        List<Image> images = MarkdownUtils.extract(request.getContent(), Image.class);
        Optional<Image> optionalImage = PostImageService.extractFirstImage(images);
        boolean hasThumbnail = optionalImage.isPresent();

        Post post = request.toEntity(
                null,
                archiveRepository.findByAuthor(archiveInfoResponse.getAuthor()).get(),
                nextSequence,
                hasThumbnail,
                null
        );

        // 썸네일 URL 생성
        String thumbnailUrl = optionalImage.map(image -> processThumbnail(image, post)).orElse(null);

        // 포스트에 썸네일 URL 부착
        post.setThumbnailUrl(thumbnailUrl);

        // 포스트 저장
        postRepository.save(post);

        // 외부 이미지 저장
        postImageService.processSaveImages(images, post);

        return new PostCreateResponse(archiveInfoResponse.getAuthor(), nextSequence);
    }

    private String processThumbnail(Image image, Post post) {
        FileResponse thumbnailFile = postThumbnailService.createAndUploadAndSaveThumbnail(image, post);
        return thumbnailFile.getVirtualUrl();
    }
}
