package kr.minimalest.core.domain.folder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("""
        SELECT f
        FROM Folder f
        JOIN FETCH f.archive a
        LEFT JOIN FETCH f.parent fp
        LEFT JOIN FETCH f.children fc
        WHERE a.author = :author
        AND f.folderStatus = 'ACTIVE'
    """)
    List<Folder> findAllByArchiveAuthor(String author);

    @Query("""
        SELECT count(f)
        FROM Folder f
        JOIN f.archive a
        WHERE a.author = :author
        AND f.folderStatus = :status
    """)
    long countByAuthor(String author, FolderStatus status);

    @Query(
        """
        SELECT f
        FROM Folder f
        JOIN FETCH f.archive a
        WHERE a.author = :author
        AND f.name = :name
        AND f.folderStatus = 'ACTIVE'
        """
    )
    Optional<Folder> findByNameAndArchiveAuthor(String name, String author);
}
