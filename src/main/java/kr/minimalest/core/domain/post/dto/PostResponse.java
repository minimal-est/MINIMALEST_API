package kr.minimalest.core.domain.post.dto;

import kr.minimalest.core.domain.file.dto.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponse {

    private String author;
    private Long sequence;
    private FileResponse fileResponse;
}
