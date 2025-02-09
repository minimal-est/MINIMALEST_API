package kr.minimalest.core.domain.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.minimalest.core.domain.post.dto.PostPreviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static kr.minimalest.core.domain.post.PostConstants.MAX_LENGTH_TITLE;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<PostPreviewResponse> findPostPreviewsByAuthor(String author, Pageable pageable) {
        String query =
                """
                SELECT p
                FROM Post AS p
                JOIN FETCH p.archive
                WHERE p.archive.author = :author
                """;

        List<Post> posts = em.createQuery(query, Post.class)
                .setParameter("author", author)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        List<PostPreviewResponse> postPreviewResponses = posts.stream()
                .map((post) -> (PostPreviewResponse.builder()
                                .author(post.getArchive().getAuthor())
                                .title(SummaryUtils.createTitleSummary(post.getTitle(), MAX_LENGTH_TITLE))
                                .summary(post.getSummary())
                                .thumbnailUrl(post.getThumbnailUrl())
                                .createdAt(post.getCreatedAt())
                                .lastModifiedAt(post.getLastModifiedAt())
                                .build())).toList();

        return new PageImpl<>(postPreviewResponses, pageable, calculateTotalCount(author));
    }

    private long calculateTotalCount(String author) {
        String query =
                """
                SELECT COUNT(p)
                FROM Post AS p
                WHERE p.archive.author = :author
                """;

        return em.createQuery(query, Long.class)
                .setParameter("author", author)
                .getSingleResult();
    }
}
