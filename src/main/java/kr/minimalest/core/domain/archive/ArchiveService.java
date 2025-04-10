package kr.minimalest.core.domain.archive;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.dto.*;
import kr.minimalest.core.domain.archive.exception.ArchiveException;
import kr.minimalest.core.domain.folder.FolderService;
import kr.minimalest.core.domain.folder.dto.FolderCreateRequest;
import kr.minimalest.core.domain.member.Member;
import kr.minimalest.core.domain.member.MemberRepository;
import kr.minimalest.core.domain.style.StyleContext;
import kr.minimalest.core.domain.style.dto.StyleRequest;
import kr.minimalest.core.domain.style.dto.StyleResponse;
import kr.minimalest.core.domain.style.service.StyleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiveService {

    private final FolderService folderService;
    private final ArchiveRepository archiveRepository;
    private final StyleService styleService;
    private final MemberRepository memberRepository;

    @Transactional
    public ArchiveCreateResponse create(ArchiveCreateRequest request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원은 존재하지 않습니다!"));
        Archive archive = request.toEntity(member);
        archiveRepository.save(archive);
        styleService.createDefaultStylesFor(archive);
        folderService.create(new FolderCreateRequest(request.getFirstFolderName()), archive.getAuthor());
        return new ArchiveCreateResponse(archive.getAuthor());
    }

    @Transactional(readOnly = true)
    public ArchiveInfoResponses findArchiveInfos(String email) {
        List<Archive> archives = archiveRepository.findAllByEmail(email);
        List<ArchiveInfoResponse> archiveInfoResponses = archives.stream()
                .map(ArchiveInfoResponse::fromEntity)
                .toList();
        return new ArchiveInfoResponses(archiveInfoResponses);
    }

    @Transactional(readOnly = true)
    public void validateArchive(String author, String email) {
        Optional<Archive> optionalArchive = archiveRepository.findByAuthorAndMemberEmail(author, email);
        Archive archive = optionalArchive.orElseThrow(() -> new IllegalArgumentException("아카이브의 계정이 올바르지 않습니다!"));
        ArchiveInfoResponse.fromEntity(archive);
    }

    @Transactional(readOnly = true)
    public ArchiveInfoResponse findArchiveInfo(String author) {
        Optional<Archive> optionalArchive = archiveRepository.findByAuthor(author);
        Archive archive = optionalArchive.orElseThrow(() -> new EntityNotFoundException("해당 작가의 아카이브는 존재하지 않습니다!"));
        return ArchiveInfoResponse.fromEntity(archive);
    }

    /**
     * 아카이브의 스타일을 대체시킵니다. 아카이브의 스타일을 바꾸는 방법은 이 메소드 단 하나 뿐입니다.
     * @param request 아카이브 스타일 요청 DTO(작가명, 스타일 정보)
     * @return 아카이브 스타일 응답 DTO
     */
    @Transactional
    public StyleResponse putStyle(String author, StyleRequest request) {
        try {

            if (request.getArchiveStyle() != null) {
                styleService.putStyle(
                        author,
                        StyleContext.ARCHIVE,
                        request.getArchiveStyle().getStyles()
                );
            }

            if (request.getPostStyle() != null) {
                styleService.putStyle(
                        author,
                        StyleContext.POST,
                        request.getPostStyle().getStyles()
                );
            }

            return StyleResponse.builder()
                    .archiveStyle(request.getArchiveStyle())
                    .postStyle(request.getPostStyle())
                    .build();
        } catch (Exception ex) {
            throw new ArchiveException("스타일을 삽입할 수 없습니다!", ex);
        }
    }
}
