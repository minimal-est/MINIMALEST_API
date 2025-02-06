package kr.minimalest.core.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostCreateRequest {

    @NotBlank(message = "제목은 빈칸이 될 수 없습니다.")
    private String title;

    private String subtitle;

    @NotBlank(message = "본문은 빈칸이 될 수 없습니다.")
    private String content;
}
