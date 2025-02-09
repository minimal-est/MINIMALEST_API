package kr.minimalest.core.domain.post;

import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.member.Member;
import kr.minimalest.core.domain.post.dto.PostViewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    """)
    List<Post> findAllByArchiveAuthor(String author);

    // Author(저자)와 Sequence(번호)로 Post 조회
    @Query("""
            SELECT p
            FROM Post AS p
            JOIN FETCH p.archive a
            WHERE a.author = :author AND p.sequence = :sequence
    """)
    Optional<Post> findWithArchive(String author, Long sequence);

    // Author(저자)로 PostViews 조회
    @Query("""
            SELECT new kr.minimalest.core.domain.post.dto.PostViewResponse(
                p.archive.author,
                p.title,
                p.content,
                p.createdAt,
                p.lastModifiedAt
            )
            FROM Post AS p
            WHERE p.archive.author = :author
    """)
    Page<PostViewResponse> findAllView(String author, Pageable pageable);

    // 해당 Archive 최대 Sequence(번호) 조회
    // 동시성 문제를 해결하기 위해 락(Lock) 걸기
    @Query("""
            SELECT COALESCE(MAX(p.sequence), 0)
            FROM Post AS p
            WHERE p.archive = :archive
    """)
    long findMaxSequenceByArchiveForUpdate(Archive archive);
}
