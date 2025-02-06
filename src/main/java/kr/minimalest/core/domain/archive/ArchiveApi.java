package kr.minimalest.core.domain.archive;

import kr.minimalest.core.domain.folder.FolderService;
import kr.minimalest.core.domain.folder.dto.FolderView;
import kr.minimalest.core.domain.post.PostService;
import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.archive.dto.ArchiveInfoResponse;
import kr.minimalest.core.domain.post.dto.PostViewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{author}/post")
    public ApiResponse<?> findAllPost(
            @PathVariable String author,
            @PageableDefault Pageable pageable
    ) {
        Page<PostViewResponse> postViewResponses = postService.findAllPostView(author, pageable);
        return ApiResponse.success(postViewResponses);
    }

    @GetMapping("/{author}/post/{sequence}")
    public ApiResponse<?> findPost(
            @PathVariable String author,
            @PathVariable Long sequence
    ) {
        PostViewResponse postViewResponse = postService.findPostView(author, sequence);
        return ApiResponse.success(postViewResponse);
    }

    @GetMapping("/{author}/folder")
    public ApiResponse<?> findAll(
            @PathVariable String author
    ) {
        List<FolderView> folderTree = folderService.getFolderTree(author);
        return ApiResponse.success(folderTree);
    }
}
