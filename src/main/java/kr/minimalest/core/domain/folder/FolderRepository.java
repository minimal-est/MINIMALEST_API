package kr.minimalest.core.domain.folder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("""
        SELECT f
        FROM Folder f
        JOIN FETCH f.archive a
        LEFT JOIN FETCH f.parent fp
        LEFT JOIN FETCH f.children fc
        WHERE a.author = :author
    """)
    List<Folder> findByArchiveAuthor(String author);
}
