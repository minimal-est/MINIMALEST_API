package kr.minimalest.core.domain.post.repository;

import kr.minimalest.core.domain.post.dto.PostPreviewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {

    Slice<PostPreviewResponse> findPostPreviewsInFolderByAuthor(String author, Long folderId, Pageable pageable);

    Slice<PostPreviewResponse> findPostPreviewsByAuthor(String author, Pageable pageable);
}
