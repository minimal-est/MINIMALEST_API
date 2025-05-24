package kr.minimalest.core.domain.post.service;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.archive.ArchiveRepository;
import kr.minimalest.core.domain.folder.Folder;
import kr.minimalest.core.domain.folder.FolderRepository;
import kr.minimalest.core.domain.post.ContentImage;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.dto.PostCreateRequest;
import kr.minimalest.core.domain.post.dto.PostCreateResponse;
import kr.minimalest.core.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCreateService implements PostCreator {

    private final PostRepository postRepository;
    private final PostImageService postImageService;
    private final ArchiveRepository archiveRepository;
    private final ThumbnailService thumbnailService;
    private final FolderRepository folderRepository;
    private final Extractor extractor;

    @Override
    @Transactional
    public PostCreateResponse create(String author, String email, PostCreateRequest request) {
        long nextSequence = getNextSequence(author);

        Post post = createAndSavePost(author, request, nextSequence);

        if (hasThumbnailUrl(request)) {
            generateThumbnailUrlAndAttachToPost(request, post);
        }

        List<ContentImage> contentImages = ContentImage.extractContentImages(request.getContent(), extractor);

        postImageService.saveImageMetadataAndAttachToPost(contentImages, post);

        return new PostCreateResponse(author, nextSequence);
    }

    private long getNextSequence(String author) {
        return postRepository.findMaxSequenceByArchive(author) + 1;
    }

    private Post createAndSavePost(String author, PostCreateRequest request, long nextSequence) {
        Post post = createPost(author, request, nextSequence);
        postRepository.save(post);
        return post;
    }

    private Post createPost(String author, PostCreateRequest request, long nextSequence) {
        Folder folder = findFolder(request.getFolderId());
        Archive archive = findArchive(author);
        return request.toEntity(folder, archive, nextSequence);
    }

    private Folder findFolder(Long folderId) {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new EntityNotFoundException("폴더가 존재하지 않습니다!"));
    }

    private Archive findArchive(String author) {
        return archiveRepository.findByAuthor(author)
                .orElseThrow(() -> new EntityNotFoundException("아카이브가 존재하지 않습니다!"));
    }

    private static boolean hasThumbnailUrl(PostCreateRequest request) {
        return StringUtils.hasText(request.getThumbnailUrl());
    }

    @Override
    @Transactional
    public PostCreateResponse update(String author, long sequence, PostCreateRequest request) {
        Post post = findPostAndUpdate(author, sequence, request);
        if (isThumbnailChanged(request, post)) {
            if (hasThumbnailUrl(request)) {
                generateThumbnailUrlAndAttachToPost(request, post);
            } else {
                post.removeThumbnail();
            }
        }

        List<ContentImage> contentImages = ContentImage.extractContentImages(request.getContent(), extractor);
        postImageService.saveImageMetadataAndAttachToPost(contentImages, post);

        return new PostCreateResponse(author, sequence);
    }

    private Post findPostAndUpdate(String author, long sequence, PostCreateRequest request) {
        Post post = findPost(author, sequence);

        post.updateTitle(request.getTitle());
        post.updateContent(request.getContent());

        Folder folder = findFolder(request.getFolderId());
        post.updateFolder(folder);
        return post;
    }

    private Post findPost(String author, long sequence) {
        return postRepository.findWithArchive(author, sequence)
                .orElseThrow(() -> new EntityNotFoundException("포스트가 존재하지 않습니다!"));
    }

    private static boolean isThumbnailChanged(PostCreateRequest request, Post post) {
        return !request.getThumbnailUrl().equals(post.getThumbnailUrl());
    }

    private void generateThumbnailUrlAndAttachToPost(PostCreateRequest request, Post post) {
        thumbnailService.generateThumbnailUrl(post, request.getThumbnailUrl());
    }
}
