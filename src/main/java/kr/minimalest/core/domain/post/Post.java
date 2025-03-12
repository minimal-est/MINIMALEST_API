package kr.minimalest.core.domain.post;

import jakarta.persistence.*;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.folder.Folder;
import kr.minimalest.core.domain.base.BaseColumn;
import lombok.*;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    // 포스트의 본문입니다.
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // 작성 순서대로 부여되는 번호입니다.
    @Column(nullable = false)
    private Long sequence;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_id", nullable = false)
    private Archive archive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = true)
    private Folder folder;

    @Column(nullable = false)
    private boolean hasThumbnail;

    @Setter
    @Column(nullable = true)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @Enumerated(EnumType.STRING)
    private PostRole postRole;

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateFolder(Folder folder) {
        this.folder = folder;
    }

    public void updateThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        this.hasThumbnail = StringUtils.hasText(thumbnailUrl);
    }

    public void updateStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }

    public void updateRole(PostRole postRole) {
        this.postRole = postRole;
    }

    public boolean isModified() {
        if (getCreatedAt() == null || getLastModifiedAt() == null) return false;
        Duration duration = Duration.between(getCreatedAt(), getLastModifiedAt());
        // 생성일과 수정일 1분 이상 차이나면 true
        return duration.toMinutes() >= 1;
    }

    public static boolean isModified(LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        if (createdAt == null || lastModifiedAt == null) return false;
        Duration duration = Duration.between(createdAt, lastModifiedAt);
        // 생성일과 수정일 1분 이상 차이나면 true
        return duration.toMinutes() >= 1;
    }
}
