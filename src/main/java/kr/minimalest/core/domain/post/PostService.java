package kr.minimalest.core.domain.post;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.archive.ArchiveRepository;
import kr.minimalest.core.domain.file.*;
import kr.minimalest.core.domain.file.File;
import kr.minimalest.core.domain.file.dto.FileResponse;
import kr.minimalest.core.domain.post.dto.PostCreateRequest;
import kr.minimalest.core.domain.post.dto.PostCreateResponse;
import kr.minimalest.core.domain.post.dto.PostResponse;
import kr.minimalest.core.domain.post.dto.PostViewResponse;
import kr.minimalest.core.domain.post.exception.PostException;
import kr.minimalest.core.domain.post.thumbnail.SimpleThumbnailGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;
import java.util.Optional;

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

    // Author와 Sequence로 Post정보를 제공합니다.
    public PostViewResponse findPostView(String author, Long sequence) {
        Optional<Post> optionalPost = postRepository.findWithArchive(author, sequence);
        Post post = optionalPost.orElseThrow(() -> new EntityNotFoundException("해당 포스트는 존재하지 않습니다!"));
        return PostViewResponse.builder()
                .author(post.getArchive().getAuthor())
                .title(post.getTitle())
                .content(post.getContent())
                .subtitle(post.getSubtitle())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();
    }

    public Page<PostViewResponse> findAllPostView(String author, Pageable pageable) {
        return postRepository.findAllView(author, pageable);
    }

    @Transactional
    public PostCreateResponse create(String email, PostCreateRequest request) {
        // 아카이브 검증
        Optional<Archive> optionalArchive = archiveRepository.findByMemberEmailWithMember(email);
        Archive archive = optionalArchive.orElseThrow(() -> new PostException("아카이브의 계정이 올바르지 않습니다!"));

        // 이미지 추출
        List<Image> images = MarkdownUtils.extract(request.getContent(), Image.class);

        // 아카이브의 Post Sequence 더하기
        long nextSequence = postRepository.findMaxSequenceByArchiveForUpdate(archive) + 1;

        // 썸네일 이미지 선정
        Optional<Image> image = extractFirstImage(images);

        boolean hasThumbnail = image.isPresent();

        String compactedThumbnailUrl;

        // Post 생성
        Post post = createAndSavePost(request, archive, nextSequence, hasThumbnail);

        // 이미지 저장
        processSaveImages(images, post);

        if (hasThumbnail) {
            // 썸네일로 사용될 이미지 정보
            String thumbnailUrl = image.get().getDestination();
            String filename = FileUtils.extractKeyParameter(thumbnailUrl);
            String pureExt = FileUtils.extractPureExt(filename);
            String contentType = toImageContentType(pureExt);

            // 선정한 이미지 압축
            OutputStream thumbnailOutputStream = thumbnailService.create(thumbnailUrl, pureExt);

            // 썸네일 저장
            PostResponse postResponse = uploadAndSave(post, thumbnailOutputStream, filename,
                    contentType);

            // 선정된 압축 썸네일 URL 추출
            compactedThumbnailUrl = postResponse.getFileResponse().getVirtualUrl();

            // Post에 썸네일 URL 설정
            post.setThumbnailUrl(compactedThumbnailUrl);
        }

        return new PostCreateResponse(archive.getAuthor(), nextSequence);
    }

    private Post createAndSavePost(PostCreateRequest request, Archive archive, long nextSequence,
                                   boolean hasThumbnail) {
        Post post = Post.builder()
                .content(request.getContent())
                .title(request.getTitle())
                .subtitle(request.getSubtitle())
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

    private static String toImageContentType(String pureExt) {
        return "image/" + pureExt;
    }

    private PostResponse uploadAndSave(Post post
                                       , OutputStream outputStream, String filename,
                                       String contentType) {
        FileResponse fileResponse = fileService.uploadAndSave(post, outputStream, filename, contentType);
        return new PostResponse(post.getArchive().getAuthor(), post.getSequence(), fileResponse);
    }

    // 본문에서 첫 이미지(마크다운)를 추출합니다.
    private static Optional<Image> extractFirstImage(List<Image> images) {
        return images.isEmpty() ? Optional.empty() : Optional.of(images.get(0));
    }
}
