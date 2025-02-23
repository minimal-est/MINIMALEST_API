package kr.minimalest.core.domain.post.service;

import kr.minimalest.core.domain.post.dto.PostCreateRequest;
import kr.minimalest.core.domain.post.dto.PostCreateResponse;

public interface PostCreator {

    PostCreateResponse create(String author, String email, PostCreateRequest request);
}
