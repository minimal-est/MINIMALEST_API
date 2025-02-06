package kr.minimalest.core.domain.post;

import jakarta.validation.Valid;
import kr.minimalest.core.common.annotation.Authenticate;
import kr.minimalest.core.common.annotation.AuthenticatedMemberEmail;
import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.post.dto.PostCreateRequest;
import kr.minimalest.core.domain.post.dto.PostCreateResponse;
import kr.minimalest.core.domain.post.dto.PostViewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostApi {

    private final PostService postService;

    @Authenticate
    @PostMapping
    public ApiResponse<?> create(
            @Valid @RequestBody PostCreateRequest postCreateRequest,
            @AuthenticatedMemberEmail String email
    ) {
        PostCreateResponse postCreateResponse = postService.create(email, postCreateRequest);
        return ApiResponse.success(postCreateResponse);
    }

    @GetMapping
    public ApiResponse<?> findAll(
            @RequestParam String author,
            Pageable pageable
    ) {
        Page<PostViewResponse> allPostView = postService.findAllPostView(author, pageable);
        return ApiResponse.success(allPostView);
    }
}
