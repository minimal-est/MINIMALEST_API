package kr.minimalest.core.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCreateResponse {

    private String author;
    private Long sequence;
}
