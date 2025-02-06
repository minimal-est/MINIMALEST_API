package kr.minimalest.core.domain.archive;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.dto.ArchiveInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiveService {

    private final ArchiveRepository archiveRepository;

    public ArchiveInfoResponse findArchiveInfo(String author) {
        Optional<Archive> optionalArchive = archiveRepository.findByAuthor(author);
        Archive archive = optionalArchive.orElseThrow(() -> new EntityNotFoundException("해당 author의 아카이브는 존재하지 않습니다."));
        return ArchiveInfoResponse.builder()
                .author(archive.getAuthor())
                .mainTitle(archive.getMainTitle())
                .subTitle(archive.getSubTitle())
                .build();
    }

}
