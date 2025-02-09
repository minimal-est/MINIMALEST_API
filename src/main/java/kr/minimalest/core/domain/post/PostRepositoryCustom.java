package kr.minimalest.core.domain.post;

import kr.minimalest.core.domain.post.dto.PostPreviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostPreviewResponse> findPostPreviewsByAuthor(String author, Pageable pageable);
}
