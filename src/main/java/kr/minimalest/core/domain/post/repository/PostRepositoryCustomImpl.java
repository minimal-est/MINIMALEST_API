package kr.minimalest.core.domain.post.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.minimalest.core.domain.post.ContentHelper;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.dto.PostPreviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final ContentHelper contentHelper;

    // 등록된 필드로만 정렬 가능하도록 설정
    // SQL 인젝션 방지
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("createdAt", "title", "author");

    @PersistenceContext
    private EntityManager em;

    @Override
    public Slice<PostPreviewResponse> findPostPreviewsInFolderByAuthor(String author, Long folderId, Pageable pageable) {
        String query =
                """
                SELECT p
                FROM Post AS p
                JOIN FETCH p.archive
                JOIN FETCH p.folder
                WHERE p.archive.author = :author AND p.folder.id = :folderId
                """;

        query = appendOrderBy(query, pageable.getSort());

        List<Post> posts = em.createQuery(query, Post.class)
                .setParameter("author", author)
                .setParameter("folderId", folderId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize() + 1)
                .getResultList();

        boolean hasNext = posts.size() > pageable.getPageSize();
        if (hasNext) {
            posts.remove(posts.size() - 1);
        }

        List<PostPreviewResponse> postPreviewResponses = toPostPreviewResponses(posts);

        return new SliceImpl<>(postPreviewResponses, pageable, hasNext);
    }

    @Override
    public Slice<PostPreviewResponse> findPostPreviewsByAuthor(String author, Pageable pageable) {
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
                .setMaxResults(pageable.getPageSize() + 1)
                .getResultList();

        boolean hasNext = posts.size() > pageable.getPageSize();
        if (hasNext) {
            posts.remove(posts.size() - 1);
        }

        List<PostPreviewResponse> postPreviewResponses = toPostPreviewResponses(posts);

        return new SliceImpl<>(postPreviewResponses, pageable, hasNext);
    }

    private List<PostPreviewResponse> toPostPreviewResponses(List<Post> posts) {
        return posts.stream()
                .map((post) -> PostPreviewResponse.fromEntity(post, contentHelper))
                .toList();
    }

    private String appendOrderBy(String query, Sort sort) {
        if (sort.isUnsorted()) return query;

        String orderBy = sort.stream()
                .filter((order) -> ALLOWED_SORT_FIELDS.contains(order.getProperty()))
                .map((order) -> "p." + order.getProperty() + " " + order.getDirection().name())
                .collect(Collectors.joining(", ", " ORDER BY ", ""));

        return query + orderBy;
    }
}
