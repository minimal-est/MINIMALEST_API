package kr.minimalest.core.domain.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    Optional<Archive> findByAuthor(String author);

    @Query("""
        SELECT a
        FROM Archive AS a
        JOIN FETCH a.member
        WHERE a.member.email = :email
    """)
    Optional<Archive> findByMemberEmailWithMember(String email);
}
