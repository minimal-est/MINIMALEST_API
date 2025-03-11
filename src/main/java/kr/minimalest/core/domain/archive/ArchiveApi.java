package kr.minimalest.core.domain.archive;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.minimalest.core.common.annotation.Authenticate;
import kr.minimalest.core.common.annotation.AuthenticatedMemberEmail;
import kr.minimalest.core.domain.folder.FolderService;
import kr.minimalest.core.domain.folder.dto.FolderView;
import kr.minimalest.core.domain.post.PostRole;
import kr.minimalest.core.domain.post.service.PostService;
import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.archive.dto.ArchiveInfoResponse;
import kr.minimalest.core.domain.post.PostStatus;
import kr.minimalest.core.domain.post.dto.PostCreateRequest;
import kr.minimalest.core.domain.post.dto.PostCreateResponse;
import kr.minimalest.core.domain.post.dto.PostPreviewResponse;
import kr.minimalest.core.domain.post.dto.PostViewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Archive API", description = "아카이브 관련 API입니다.")
@Slf4j
@RestController
@RequestMapping("/api/archive")
@RequiredArgsConstructor
public class ArchiveApi {

    private final ArchiveService archiveService;
    private final PostService postService;
    private final FolderService folderService;

    @GetMapping("/{author}")
    public ApiResponse<?> findArchive(
            @PathVariable String author
    ) {
        ArchiveInfoResponse archiveInfoResponse = archiveService.findArchiveInfo(author);
        return ApiResponse.success(archiveInfoResponse);
    }

    @Authenticate
    @PostMapping("/{author}/post")
    public ApiResponse<?> createPost(
            @PathVariable String author,
            @AuthenticatedMemberEmail String email,
            @Valid @RequestBody PostCreateRequest postCreateRequest
    ) {
        archiveService.validateArchive(author, email);
        folderService.validateFolder(postCreateRequest.getFolderId());
        PostCreateResponse postCreateResponse = postService.create(author, email, postCreateRequest);
        return ApiResponse.success(postCreateResponse);
    }

    @GetMapping("/{author}/post/{sequence}")
    public ApiResponse<?> findPost(
            @PathVariable String author,
            @PathVariable long sequence
    ) {
        PostViewResponse postViewResponse = postService.findPostView(author, sequence);
        return ApiResponse.success(postViewResponse);
    }

    @GetMapping("/{author}/post/role/{postRole}")
    public ApiResponse<?> findAllPostWithRole(
            @PathVariable String author,
            @PathVariable PostRole postRole,
            @PageableDefault Pageable pageable
    ) {
        Slice<PostViewResponse> postViewResponses = postService.findAllPostViewWithRole(author, postRole, pageable);
        return ApiResponse.success(postViewResponses);
    }

    @Authenticate
    @DeleteMapping("/{author}/post/{sequence}")
    public ApiResponse<?> deletePost(
            @PathVariable String author,
            @PathVariable long sequence
    ) {
        postService.updatePostStatus(author, sequence, PostStatus.DELETED);
        return ApiResponse.success("성공적으로 삭제되었습니다.");
    }

    @Authenticate
    @PutMapping("/{author}/post/{sequence}")
    public ApiResponse<?> modifyPost(
            @PathVariable String author,
            @PathVariable long sequence,
            @Valid @RequestBody PostCreateRequest postCreateRequest
    ) {
        PostCreateResponse postCreateResponse = postService.updatePost(author, sequence, postCreateRequest);
        return ApiResponse.success(postCreateResponse);
    }

    @GetMapping("/{author}/post/preview")
    public ApiResponse<?> findAllPostPreview(
            @PathVariable String author,
            Pageable pageable
    ) {
        Slice<PostPreviewResponse> postPreviewResponses = postService.findAllPostPreview(author, pageable);
        return ApiResponse.success(postPreviewResponses);
    }

    @GetMapping("/{author}/post/{sequence}/preview")
    public ApiResponse<?> findPostPreview(
            @PathVariable String author,
            @PathVariable long sequence
    ) {
        PostPreviewResponse postPreviewResponse = postService.findPostPreview(author, sequence);
        return ApiResponse.success(postPreviewResponse);
    }

    @Authenticate
    @PutMapping("/{author}/post/{sequence}/role/REPRESENTATIVE")
    public ApiResponse<?> setRepresentativeRole(
            @PathVariable String author,
            @PathVariable long sequence,
            @AuthenticatedMemberEmail String email
    ) {
        archiveService.validateArchive(author, email);
        postService.setRepresentative(author, sequence);
        return ApiResponse.success("성공적으로 반영되었습니다.");
    }

    @Authenticate
    @PutMapping("/{author}/post/{sequence}/role/NONE")
    public ApiResponse<?> setNoneRole(
            @PathVariable String author,
            @PathVariable long sequence,
            @AuthenticatedMemberEmail String email
    ) {
        archiveService.validateArchive(author, email);
        postService.setNone(author, sequence);
        return ApiResponse.success("성공적으로 반영되었습니다.");
    }

    @GetMapping("/{author}/folder/{folderId}/post")
    public ApiResponse<?> findAllPostInFolder(
            @PathVariable String author,
            @PathVariable Long folderId,
            Pageable pageable
    ) {
        Slice<PostPreviewResponse> postPreviewResponses = postService.findAllPostViewInFolder(author, folderId, pageable);
        return ApiResponse.success(postPreviewResponses);
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
