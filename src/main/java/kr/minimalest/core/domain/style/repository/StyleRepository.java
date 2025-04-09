package kr.minimalest.core.domain.style.repository;

import kr.minimalest.core.domain.style.StyleContext;
import kr.minimalest.core.domain.style.entity.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StyleRepository extends JpaRepository<Style, Long> {

    @Query("""
            SELECT s
            FROM Style AS s
            JOIN s.archive a
            WHERE s.archive.author = :author
            AND s.domainType = :domainType
    """)
    Optional<Style> findByAuthorAndContext(String author, StyleContext domainType);
}
