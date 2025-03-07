package kr.minimalest.core.domain.post.repository;

import kr.minimalest.core.domain.folder.Folder;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.PostRole;
import kr.minimalest.core.domain.post.dto.PostViewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    // Author(저자)로 Posts 조회
    @Query("""
            SELECT p
            FROM Post AS p
            JOIN FETCH p.archive a
            JOIN FETCH p.folder
            WHERE a.author = :author
            AND p.postStatus = 'PUBLISHED'
    """)
    List<Post> findAllByArchiveAuthor(String author);

    // Author(저자)와 Sequence(번호)로 Post 조회
    @Query("""
            SELECT p
            FROM Post AS p
            JOIN FETCH p.archive a
            WHERE a.author = :author AND p.sequence = :sequence
            AND p.postStatus = 'PUBLISHED'
    """)
    Optional<Post> findWithArchive(String author, long sequence);

    // Author(저자)로 PostViews 조회
    @Query("""
            SELECT new kr.minimalest.core.domain.post.dto.PostViewResponse(
                p.archive.author,
                p.title,
                p.content,
                p.folder.id,
                p.folder.name,
                p.postRole,
                p.createdAt,
                p.lastModifiedAt
            )
            FROM Post AS p
            WHERE p.archive.author = :author
            AND p.postStatus = 'PUBLISHED'
    """)
    Slice<PostViewResponse> findAllView(String author, Pageable pageable);

    // 해당 Archive 최대 Sequence(번호) 조회
    // 동시성 문제를 해결하기 위해 락(Lock) 걸기
    @Query("""
            SELECT COALESCE(MAX(p.sequence), 0)
            FROM Post AS p
            WHERE p.archive.author = :author
    """)
    long findMaxSequenceByArchive(String author);

    @Query("""
            SELECT p.folder
            FROM Post AS p
            WHERE p = :post
    """)
    Optional<Folder> findFolder(Post post);

    @Query("""
            SELECT p
            FROM Post AS p
            JOIN FETCH p.archive a
            JOIN FETCH p.folder f
            WHERE p.archive.author = :author
            AND p.postRole = :postRole
            AND p.postStatus = 'PUBLISHED'
    """)
    Optional<Post> findWithRole(String author, PostRole postRole);
}
