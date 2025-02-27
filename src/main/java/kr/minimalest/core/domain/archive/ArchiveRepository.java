package kr.minimalest.core.domain.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    @Query("""
        SELECT a
        FROM Archive AS a
        JOIN FETCH a.member m
        JOIN FETCH m.profile p
        WHERE a.author = :author
    """)
    Optional<Archive> findByAuthor(String author);

    @Query("""
        SELECT a
        FROM Archive AS a
        JOIN FETCH a.member m
        JOIN FETCH m.profile p
        WHERE a.author = :author AND m.email = :email
    """)
    Optional<Archive> findByAuthorAndMemberEmail(String author, String email);
}
