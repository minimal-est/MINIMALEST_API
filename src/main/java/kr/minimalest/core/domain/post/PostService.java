package kr.minimalest.core.domain.post;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.post.dto.*;
import kr.minimalest.core.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostCreator postCreator;
    private final ContentHelper contentHelper;

    public Slice<PostPreviewResponse> findAllPostViewInFolder(String author, Long folderId, @Nullable Pageable pageable) {
        return postRepository.findPostPreviewsInFolderByAuthor(author, folderId, pageable);
    }

    @Transactional(readOnly = true)
    public Slice<PostPreviewResponse> findAllPostPreview(String author, @Nullable Pageable pageable) {
        return postRepository.findPostPreviewsByAuthor(author, pageable);
    }

    @Transactional(readOnly = true)
    public PostPreviewResponse findPostPreview(String author, Long sequence) {
        Post post = validateAndFindPost(author, sequence);
        return PostPreviewResponse.fromEntity(post, contentHelper);
    }

    @Transactional(readOnly = true)
    public Slice<PostViewResponse> findAllPostView(String author, @Nullable Pageable pageable) {
        return postRepository.findAllView(author, pageable);
    }

    @Transactional(readOnly = true)
    public PostViewResponse findPostView(String author, Long sequence) {
        Post post = validateAndFindPost(author, sequence);
        return PostViewResponse.fromEntity(post);
    }

    @Transactional
    public PostCreateResponse create(String author, String email, PostCreateRequest request) {
        return postCreator.create(author, email, request);
    }

    // Entity를 반환하는 메소드를 컴포넌트로 분리하기엔 부담이.. 어쩌지?
    private Post validateAndFindPost(String author, long sequence) {
        Optional<Post> optionalPost = postRepository.findWithArchive(author, sequence);
        return optionalPost.orElseThrow(() -> new EntityNotFoundException("해당 포스트는 존재하지 않습니다!"));
    }
}
