package kr.minimalest.core.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.folder.Folder;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.PostConstants;
import kr.minimalest.core.domain.post.SummaryUtils;
import lombok.Data;

@Data
public class PostCreateRequest {

    @NotBlank(message = "제목은 빈칸이 될 수 없습니다.")
    private String title;

    @NotBlank(message = "본문은 빈칸이 될 수 없습니다.")
    private String content;

    public Post toEntity(Folder folder, Archive archive, long sequence, boolean hasThumbnail, String thumbnailUrl) {
        return Post.builder()
                .title(title)
                .summary(SummaryUtils.createContentSummary(content, PostConstants.MAX_LENGTH_SUMMARY))
                .content(content)
                .sequence(sequence)
                .archive(archive)
                .folder(folder)
                .hasThumbnail(hasThumbnail)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}
