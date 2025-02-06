package kr.minimalest.core.domain.folder;

import kr.minimalest.core.domain.folder.dto.FolderView;
import kr.minimalest.core.domain.folder.dto.FolderWithPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("""
        SELECT new kr.minimalest.core.domain.folder.dto.FolderView(
            f.id,
            f.name,
            f.depth,
            f.parent.id,
            CASE WHEN f.parent IS NULL THEN true ELSE false END,
            CASE WHEN f.children IS EMPTY THEN true ELSE false END
        )
        FROM Folder f
        JOIN f.archive a
        WHERE a.author = :author
    """)
    List<FolderView> findFolderViewByAuthor(String author);

    @Query("""
        SELECT new kr.minimalest.core.domain.folder.dto.FolderWithPost(
            p.id,
            p.title
        )
        FROM Post p
        JOIN p.folder f
        WHERE f.id = :folderId
    """)
    List<FolderWithPost> findPostsById(Long folderId);
}
