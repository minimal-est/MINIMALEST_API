package kr.minimalest.core.domain.archive;

import jakarta.validation.Valid;
import kr.minimalest.core.common.annotation.Authenticate;
import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.folder.FolderService;
import kr.minimalest.core.domain.folder.FolderStatus;
import kr.minimalest.core.domain.folder.dto.FolderCreateRequest;
import kr.minimalest.core.domain.folder.dto.FolderCreateResponse;
import kr.minimalest.core.domain.folder.dto.FolderView;
import kr.minimalest.core.domain.post.dto.PostPreviewResponse;
import kr.minimalest.core.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/archive")
@RequiredArgsConstructor
public class ArchiveFolderApi {

    private final PostService postService;
    private final FolderService folderService;

    @GetMapping("/{author}/folder/{folderId}/post")
    public ApiResponse<?> findAllPostInFolder(
            @PathVariable String author,
            @PathVariable Long folderId,
            Pageable pageable
    ) {
        Slice<PostPreviewResponse> postPreviewResponses = postService.findAllPostViewInFolder(author, folderId, pageable);
        return ApiResponse.success(postPreviewResponses);
    }

    @Authenticate
    @PostMapping("/{author}/folder")
    public ApiResponse<?> createFolder(
            @PathVariable String author,
            @Valid @RequestBody FolderCreateRequest folderCreateRequest
    ) {
        try {
            FolderCreateResponse folderCreateResponse = folderService.create(folderCreateRequest, author);
            return ApiResponse.success(folderCreateResponse);
        } catch (IllegalArgumentException ex) {
            return ApiResponse.error(HttpStatus.CONFLICT, ex.getMessage());
        }
    }

    @Authenticate
    @DeleteMapping("/{author}/folder/{id}")
    public ApiResponse<?> deleteFolder(
            @PathVariable String author,
            @PathVariable Long id
    ) {
        try {
            folderService.updateFolderStatus(author, id, FolderStatus.DELETED);
            return ApiResponse.success("폴더를 성공적으로 삭제했습니다.");
        } catch (IllegalStateException ex) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping("/{author}/folder/flat")
    public ApiResponse<?> findFolderFlat(
            @PathVariable String author
    ) {
        List<FolderView> folderFlat = folderService.getFlatFolders(author);
        return ApiResponse.success(folderFlat);
    }

    @GetMapping("/{author}/folder/tree")
    public ApiResponse<?> findFolderTree(
            @PathVariable String author
    ) {
        List<FolderView> folderTree = folderService.getFolderTree(author);
        return ApiResponse.success(folderTree);
    }
}
