package kr.minimalest.core.domain.post;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.archive.ArchiveRepository;
import kr.minimalest.core.domain.file.*;
import kr.minimalest.core.domain.file.File;
import kr.minimalest.core.domain.file.dto.FileResponse;
import kr.minimalest.core.domain.post.dto.*;
import kr.minimalest.core.domain.post.thumbnail.SimpleThumbnailGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;
import java.util.Optional;

import static kr.minimalest.core.domain.post.PostConstants.MAX_LENGTH_SUMMARY;
import static kr.minimalest.core.domain.post.PostConstants.MAX_LENGTH_TITLE;
import static kr.minimalest.core.domain.post.SummaryUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final SimpleThumbnailGenerator thumbnailService;
    private final FileService fileService;
    private final FileStorageService fileStorageService;
    private final FileRepository fileRepository;
    private final PostRepository postRepository;
    private final ArchiveRepository archiveRepository;

    @Transactional(readOnly = true)
    public Page<PostPreviewResponse> findAllPostPreview(String author, @Nullable Pageable pageable) {
        return postRepository.findPostPreviewsByAuthor(author, pageable);
    }

    @Transactional(readOnly = true)
    public PostPreviewResponse findPostPreview(String author, Long sequence) {
        Post post = validateAndFindPost(author, sequence);
        return PostPreviewResponse.builder()
                .author(post.getArchive().getAuthor())
                .title(createTitleSummary(post.getTitle(), MAX_LENGTH_TITLE))
                .summary(post.getSummary())
                .thumbnailUrl(post.getThumbnailUrl())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<PostViewResponse> findAllPostView(String author, @Nullable Pageable pageable) {
        return postRepository.findAllView(author, pageable);
    }

    @Transactional(readOnly = true)
    public PostViewResponse findPostView(String author, Long sequence) {
        Post post = validateAndFindPost(author, sequence);
        return PostViewResponse.builder()
                .author(post.getArchive().getAuthor())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();
    }

    @Transactional
    public PostCreateResponse create(String email, PostCreateRequest request) {
        // 아카이브 검증
        Archive archive = validateArchive(email);

        // 이미지 추출
        List<Image> images = MarkdownUtils.extract(request.getContent(), Image.class);

        // 아카이브의 Post Sequence 더하기
        long nextSequence = findNextSequence(archive);

        // 썸네일 이미지 선정
        Optional<Image> optionalImage = extractFirstImage(images);

        // 썸네일 존재 여부
        boolean hasThumbnail = optionalImage.isPresent();

        // Post 생성
        Post post = createAndSavePost(request, archive, nextSequence, hasThumbnail);

        // 이미지 저장
        processSaveImages(images, post);

        // 압축 썸네일 URL
        String compactedThumbnailUrl;

        if (hasThumbnail) {
            Image image = optionalImage.get();

            // 압축 썸네일 생성, 업로드, 저장
            PostResponse postResponse = createAndUploadAndSaveThumbnail(image, post);

            // 선정된 압축 썸네일 URL 추출
            compactedThumbnailUrl = postResponse.getFileResponse().getVirtualUrl();

            // 포스트 썸네일 URL 설정
            post.setThumbnailUrl(compactedThumbnailUrl);
        }

        return new PostCreateResponse(archive.getAuthor(), nextSequence);
    }

    private Post validateAndFindPost(String author, Long sequence) {
        Optional<Post> optionalPost = postRepository.findWithArchive(author, sequence);
        return optionalPost.orElseThrow(() -> new EntityNotFoundException("해당 포스트는 존재하지 않습니다!"));
    }

    private PostResponse createAndUploadAndSaveThumbnail(Image image, Post post) {
        // 썸네일로 사용될 이미지 정보
        String thumbnailUrl = image.getDestination();
        String filename = FileUtils.extractKeyParameter(thumbnailUrl);
        String pureExt = FileUtils.extractPureExt(filename);
        String contentType = toImageContentType(pureExt);

        // 선정한 이미지 압축
        OutputStream thumbnailOutputStream = thumbnailService.create(thumbnailUrl, pureExt);

        // 썸네일 저장
        return uploadAndSave(post, thumbnailOutputStream, filename, contentType);
    }

    private long findNextSequence(Archive archive) {
        return postRepository.findMaxSequenceByArchiveForUpdate(archive) + 1;
    }

    private Archive validateArchive(String email) {
        Optional<Archive> optionalArchive = archiveRepository.findByMemberEmailWithMember(email);
        return optionalArchive.orElseThrow(() -> new IllegalArgumentException("아카이브의 계정이 올바르지 않습니다!"));
    }

    private Post createAndSavePost(PostCreateRequest request, Archive archive, long nextSequence,
                                   boolean hasThumbnail) {
        // 요약 정보가 비어있을 시 채우기
        String postSummary;
        if (StringUtils.hasText(request.getSummary())) {
            postSummary = request.getSummary();
        } else {
            postSummary = createContentSummary(request.getContent(), MAX_LENGTH_SUMMARY);
        }

        Post post = Post.builder()
                .content(request.getContent())
                .title(request.getTitle())
                .summary(postSummary)
                .archive(archive)
                .sequence(nextSequence)
                .hasThumbnail(hasThumbnail)
                .build();

        postRepository.save(post);
        return post;
    }

    private void processSaveImages(List<Image> contentImages, Post post) {
        List<String> contentImageUrls = contentImages.stream().map(Image::getDestination).toList();
        List<File> savedImageFiles = fileRepository.findAllByVirtualUrls(contentImageUrls);
        List<String> savedImageUrls = savedImageFiles.stream().map(File::getVirtualUrl).toList();

        // 내부 이미지 Post 부착
        for (File savedImageFile : savedImageFiles) {
            savedImageFile.attachPost(post);
        }

        // 외부에서 부착한 이미지라면 지금 저장 (DB에 없기 때문에)
        for (Image contentImage : contentImages) {
            String contentImageUrl = contentImage.getDestination();
            if (!savedImageUrls.contains(contentImage.getDestination())) {
                saveExternalImage(contentImageUrl, post);
            }
        }
    }

    private void saveExternalImage(String contentImageUrl, Post post) {
        String pureExt = FileUtils.extractExt(contentImageUrl);
        String contentType = toImageContentType(pureExt);
        String type = FileUtils.extractType(contentType);
        String key = FileUtils.keyResolver(type, pureExt);
        fileStorageService.saveMetadataFromExternal(key, contentImageUrl, post);
    }

    private PostResponse uploadAndSave(Post post
                                       , OutputStream outputStream, String filename,
                                       String contentType) {
        FileResponse fileResponse = fileService.uploadAndSave(post, outputStream, filename, contentType);
        return new PostResponse(post.getArchive().getAuthor(), post.getSequence(), fileResponse);
    }

    private static String toImageContentType(String pureExt) {
        return "image/" + pureExt;
    }

    // 본문에서 첫 이미지(마크다운)를 추출합니다.
    private static Optional<Image> extractFirstImage(List<Image> images) {
        return images.isEmpty() ? Optional.empty() : Optional.of(images.get(0));
    }
}
