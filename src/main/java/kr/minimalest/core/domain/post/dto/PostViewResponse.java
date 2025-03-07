package kr.minimalest.core.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.PostRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class PostViewResponse {

    private String author;
    private String title;
    private String content;
    private Long folderId;
    private String folderName;
    private PostRole postRole;
    private boolean isModified;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastModifiedAt;

    public PostViewResponse(
            String author,
            String title,
            String content,
            Long folderId,
            String folderName,
            PostRole postRole,
            LocalDateTime createdAt,
            LocalDateTime lastModifiedAt) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.folderId = folderId;
        this.folderName = folderName;
        this.postRole = postRole;
        this.isModified = Post.isModified(createdAt, lastModifiedAt);
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public static PostViewResponse fromEntity(Post post) {
        return PostViewResponse.builder()
                .author(post.getArchive().getAuthor())
                .title(post.getTitle())
                .content(post.getContent())
                .folderId(post.getFolder().getId())
                .folderName(post.getFolder().getName())
                .postRole(post.getPostRole())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .isModified(post.isModified())
                .build();
    }
}
