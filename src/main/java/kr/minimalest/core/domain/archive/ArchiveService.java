package kr.minimalest.core.domain.archive;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.dto.ArchiveInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiveService {

    private final ArchiveRepository archiveRepository;

    @Transactional(readOnly = true)
    public ArchiveInfoResponse validateArchive(String author, String email) {
        Optional<Archive> optionalArchive = archiveRepository.findByAuthorAndMemberEmail(author, email);
        Archive archive = optionalArchive.orElseThrow(() -> new IllegalArgumentException("아카이브의 계정이 올바르지 않습니다!"));
        return ArchiveInfoResponse.fromEntity(archive);
    }

    @Transactional(readOnly = true)
    public ArchiveInfoResponse findArchiveInfo(String author) {
        Optional<Archive> optionalArchive = archiveRepository.findByAuthor(author);
        Archive archive = optionalArchive.orElseThrow(() -> new EntityNotFoundException("해당 작가의 아카이브는 존재하지 않습니다!"));
        return ArchiveInfoResponse.fromEntity(archive);
    }
}
