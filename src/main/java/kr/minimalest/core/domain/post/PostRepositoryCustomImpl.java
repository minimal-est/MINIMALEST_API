package kr.minimalest.core.domain.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.minimalest.core.domain.post.dto.PostPreviewResponse;
import kr.minimalest.core.domain.post.dto.PostViewResponse;
import org.springframework.data.domain.*;

import java.util.List;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Slice<PostViewResponse> findPostViewsInFolderByAuthor(String author, Long folderId, Pageable pageable) {
        String query =
                """
                SELECT p
                FROM Post AS p
                JOIN FETCH p.archive
                JOIN FETCH p.folder
                WHERE p.archive.author = :author AND p.folder.id = :folderId
                """;

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

        List<PostViewResponse> postViewResponses = posts.stream()
                .map(PostViewResponse::fromEntity)
                .toList();

        return new SliceImpl<>(postViewResponses, pageable, hasNext);
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

        List<Post> posts = em.createQuery(query, Post.class)
                .setParameter("author", author)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize() + 1)
                .getResultList();

        boolean hasNext = posts.size() > pageable.getPageSize();
        if (hasNext) {
            posts.remove(posts.size() - 1);
        }

        List<PostPreviewResponse> postPreviewResponses = posts.stream()
                .map(PostPreviewResponse::fromEntity)
                .toList();

        return new SliceImpl<>(postPreviewResponses, pageable, hasNext);
    }
}
