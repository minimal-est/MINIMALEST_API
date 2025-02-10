package kr.minimalest.core.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.minimalest.core.domain.post.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostViewResponse {

    private String author;
    private String title;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime lastModifiedAt;

    public static PostViewResponse fromEntity(Post post) {
        return PostViewResponse.builder()
                .author(post.getArchive().getAuthor())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }
}
