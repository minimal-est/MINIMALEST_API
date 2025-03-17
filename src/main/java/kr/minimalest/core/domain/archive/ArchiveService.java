package kr.minimalest.core.domain.archive;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.dto.ArchiveCreateRequest;
import kr.minimalest.core.domain.archive.dto.ArchiveCreateResponse;
import kr.minimalest.core.domain.archive.dto.ArchiveInfoResponse;
import kr.minimalest.core.domain.folder.FolderService;
import kr.minimalest.core.domain.folder.dto.FolderCreateRequest;
import kr.minimalest.core.domain.member.Member;
import kr.minimalest.core.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiveService {

    private final FolderService folderService;
    private final ArchiveRepository archiveRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ArchiveCreateResponse create(ArchiveCreateRequest request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원은 존재하지 않습니다!"));
        Archive archive = request.toEntity(member);
        archiveRepository.save(archive);
        folderService.create(new FolderCreateRequest(request.getFirstFolderName()), archive.getAuthor());
        return new ArchiveCreateResponse(archive.getAuthor());
    }

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
