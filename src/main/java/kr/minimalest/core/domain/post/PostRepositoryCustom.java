package kr.minimalest.core.domain.post;

import kr.minimalest.core.domain.post.dto.PostPreviewResponse;
import kr.minimalest.core.domain.post.dto.PostViewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {

    Slice<PostViewResponse> findPostViewsInFolderByAuthor(String author, Long folderId, Pageable pageable);

    Slice<PostPreviewResponse> findPostPreviewsByAuthor(String author, Pageable pageable);
}
