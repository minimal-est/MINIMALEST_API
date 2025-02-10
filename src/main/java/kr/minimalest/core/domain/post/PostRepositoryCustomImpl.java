package kr.minimalest.core.domain.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.minimalest.core.domain.post.dto.PostPreviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

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

        query = appendOrderBy(query, pageable.getSort());

        List<Post> posts = em.createQuery(query, Post.class)
                .setParameter("author", author)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        List<PostPreviewResponse> postPreviewResponses = posts.stream()
                .map(PostPreviewResponse::fromEntity).toList();

        return new PageImpl<>(postPreviewResponses, pageable, calculateTotalCount(author));
    }

    private static String appendOrderBy(String query, Sort sort) {
        if (sort.isUnsorted()) {
            return query;
        }
        return query + sort.stream()
                .map((order) -> (String.format("p.%s %s", order.getProperty(), order.getDirection().name())))
                .collect(Collectors.joining(", ", " ORDER BY ", ""));
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
