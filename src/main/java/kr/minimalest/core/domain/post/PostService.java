package kr.minimalest.core.domain.post;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.folder.Folder;
import kr.minimalest.core.domain.folder.FolderRepository;
import kr.minimalest.core.domain.post.dto.*;
import kr.minimalest.core.domain.post.repository.PostRepository;
import kr.minimalest.core.domain.post.service.ContentHelper;
import kr.minimalest.core.domain.post.service.PostCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FolderRepository folderRepository;
    private final PostCreator postCreator;
    private final ContentHelper contentHelper;

    @Transactional(readOnly = true)
    public Slice<PostPreviewResponse> findAllPostViewInFolder(String author, Long folderId, @Nullable Pageable pageable) {
        return postRepository.findPostPreviewsInFolderByAuthor(author, folderId, pageable);
    }

    @Transactional(readOnly = true)
    public Slice<PostPreviewResponse> findAllPostPreview(String author, @Nullable Pageable pageable) {
        return postRepository.findPostPreviewsByAuthor(author, pageable);
    }

    @Transactional(readOnly = true)
    public PostPreviewResponse findPostPreview(String author, Long sequence) {
        Post post = findPost(author, sequence);
        Folder folder = findFolder(post);
        return PostPreviewResponse.fromEntity(post, folder.getName(), contentHelper);
    }

    @Transactional(readOnly = true)
    public Slice<PostViewResponse> findAllPostView(String author, @Nullable Pageable pageable) {
        return postRepository.findAllView(author, pageable);
    }

    @Transactional(readOnly = true)
    public PostViewResponse findPostView(String author, Long sequence) {
        Post post = findPost(author, sequence);
        return PostViewResponse.fromEntity(post);
    }

    @Transactional
    public void updatePostStatus(String author, long sequence, PostStatus status) {
        Post post = findPost(author, sequence);
        if (post.getPostStatus() == status) return;
        post.updateStatus(PostStatus.DELETED);
    }

    @Transactional
    public PostCreateResponse updatePost(String author, long sequence, PostCreateRequest postCreateRequest) {
        return postCreator.update(author, sequence, postCreateRequest);
    }

    public PostViewResponse findPostViewWithRole(String author, PostRole postRole) {
        Post repPost = postRepository.findWithRole(author, postRole)
                .orElseThrow(() -> new EntityNotFoundException(postRole.name() + " 포스트가 존재하지 않습니다!"));
        return PostViewResponse.fromEntity(repPost);
    }

    @Transactional
    public PostCreateResponse create(String author, String email, PostCreateRequest request) {
        return postCreator.create(author, email, request);
    }

    @Transactional
    public void setRepresentative(String author, long sequence) {
        Post post = findPost(author, sequence);

        postRepository.findWithRole(author, PostRole.REPRESENTATIVE).ifPresent((representativePost) -> {
            if (!post.equals(representativePost)) {
                representativePost.updateRole(PostRole.NONE);
            }
        });

        if (post.getPostRole() != PostRole.REPRESENTATIVE) {
            post.updateRole(PostRole.REPRESENTATIVE);
        }
    }

    @Transactional
    public void setNone(String author, long sequence) {
        Post post = findPost(author, sequence);
        post.updateRole(PostRole.NONE);
    }

    private Post findPost(String author, long sequence) {
        return postRepository.findWithArchive(author, sequence)
                .orElseThrow(() -> new EntityNotFoundException("해당 포스트는 존재하지 않습니다!"));
    }

    private Folder findFolder(Post post) {
        return postRepository.findFolder(post)
                .orElseThrow(() -> new EntityNotFoundException("해당 포스트의 폴더가 존재하지 않습니다!"));
    }

    private Folder findFolder(Long folderId) {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new EntityNotFoundException("해당 폴더가 존재하지 않습니다!"));
    }
}
