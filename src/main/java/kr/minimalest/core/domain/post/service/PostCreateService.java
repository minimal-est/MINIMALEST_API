package kr.minimalest.core.domain.post.service;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.archive.ArchiveRepository;
import kr.minimalest.core.domain.folder.Folder;
import kr.minimalest.core.domain.folder.FolderRepository;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.dto.PostCreateRequest;
import kr.minimalest.core.domain.post.dto.PostCreateResponse;
import kr.minimalest.core.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Image;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCreateService implements PostCreator {

    private final PostRepository postRepository;
    private final ArchiveRepository archiveRepository;
    private final FolderRepository folderRepository;
    private final PostImageService postImageService;
    private final ThumbnailService thumbnailService;
    private final Extractor extractor;

    @Override
    @Transactional
    public PostCreateResponse create(String author, String email, PostCreateRequest request) {
        // 폴더 및 아카이브 검증
        Folder folder = findFolder(request.getFolderId());

        Archive archive = findArchive(author);

        // 아카이브의 Post Sequence 더하기
        long nextSequence = postRepository.findMaxSequenceByArchive(author) + 1;

        // 포스트 생성 및 저장
        Post post = request.toEntity(folder, archive, nextSequence, null);
        postRepository.save(post);

        // 썸네일 생성 및 부착
        if (StringUtils.hasText(request.getThumbnailUrl())) {
            thumbnailService.generateThumbnailUrl(post, request.getThumbnailUrl());
        }

        // 내부 이미지를 업로드 및 포스트에 부착
        List<Image> images = extractor.extract(request.getContent(), Image.class);
        postImageService.processSaveImages(images, post);

        return new PostCreateResponse(author, nextSequence);
    }

    @Override
    @Transactional
    public PostCreateResponse update(String author, long sequence, PostCreateRequest request) {
        Post post = findPost(author, sequence);
        Folder folder = findFolder(request.getFolderId());

        // 변경사항 수정
        if (!request.getThumbnailUrl().equals(post.getThumbnailUrl())) {
            // 썸네일 업데이트
            if (StringUtils.hasText(request.getThumbnailUrl())) {
                thumbnailService.generateThumbnailUrl(post, request.getThumbnailUrl());
            } else {
                post.setThumbnailUrl(null);
            }
        }

        post.updateTitle(request.getTitle());
        post.updateContent(request.getContent());
        post.updateFolder(folder);

        // 내부 이미지를 업로드 및 포스트에 부착
        List<Image> images = extractor.extract(request.getContent(), Image.class);
        postImageService.processSaveImages(images, post);

        return new PostCreateResponse(author, sequence);
    }

    private Post findPost(String author, long sequence) {
        return postRepository.findWithArchive(author, sequence)
                .orElseThrow(() -> new EntityNotFoundException("포스트가 존재하지 않습니다!"));
    }

    private Archive findArchive(String author) {
        return archiveRepository.findByAuthor(author)
                .orElseThrow(() -> new EntityNotFoundException("아카이브가 존재하지 않습니다!"));
    }

    private Folder findFolder(Long folderId) {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new EntityNotFoundException("폴더가 존재하지 않습니다!"));
    }
}
