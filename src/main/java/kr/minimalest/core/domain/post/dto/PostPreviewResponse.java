package kr.minimalest.core.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.PostConstants;
import kr.minimalest.core.domain.post.SummaryUtils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "포스트 미리보기 DTO")
@Data
@Builder
public class PostPreviewResponse {

    @Schema(description = "저자", example = "31n5ang")
    private String author;

    @Schema(description = "제목", example = "포스트 제목입니다.")
    private String title;

    @Schema(description = "요약", example = "포스트 본문입니다...")
    private String summary;

    @Schema(description = "썸네일 URL", example = "http://example.com/thumbnail.png")
    private String thumbnailUrl;

    @Schema(description = "포스트 생성 일시", example = "2025-02-08 13:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @Schema(description = "포스트 최종 수정 일시", example = "2025-02-08 15:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastModifiedAt;

    public static PostPreviewResponse fromEntity(Post post) {
        return PostPreviewResponse.builder()
                .author(post.getArchive().getAuthor())
                .title(SummaryUtils.createTitleSummary(post.getTitle(), PostConstants.MAX_LENGTH_TITLE))
                .summary(post.getSummary())
                .thumbnailUrl(post.getThumbnailUrl())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();
    }
}
