package kr.minimalest.core.domain.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    @Query("""
        SELECT f
        FROM File f
        WHERE f.virtualUrl IN :virtualUrls
    """)
    List<File> findAllByVirtualUrls(List<String> virtualUrls);
}
