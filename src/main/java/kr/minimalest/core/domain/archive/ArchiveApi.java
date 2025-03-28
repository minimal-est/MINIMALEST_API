package kr.minimalest.core.domain.archive;

import jakarta.validation.Valid;
import kr.minimalest.core.common.annotation.Authenticate;
import kr.minimalest.core.common.annotation.AuthenticatedMemberEmail;
import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.archive.dto.ArchiveCreateRequest;
import kr.minimalest.core.domain.archive.dto.ArchiveCreateResponse;
import kr.minimalest.core.domain.archive.dto.ArchiveInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/archive")
@RequiredArgsConstructor
public class ArchiveApi {

    private final ArchiveService archiveService;

    @Authenticate
    @PostMapping
    public ApiResponse<?> create(
            @AuthenticatedMemberEmail String email,
            @Valid @RequestBody ArchiveCreateRequest archiveCreateRequest
    ) {
        ArchiveCreateResponse archiveCreateResponse = archiveService.create(archiveCreateRequest, email);
        return ApiResponse.success(archiveCreateResponse);
    }

    @GetMapping("/{author}")
    public ApiResponse<?> findArchive(
            @PathVariable String author
    ) {
        ArchiveInfoResponse archiveInfoResponse = archiveService.findArchiveInfo(author);
        return ApiResponse.success(archiveInfoResponse);
    }
}
