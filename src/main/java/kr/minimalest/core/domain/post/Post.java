package kr.minimalest.core.domain.post;

import jakarta.persistence.*;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.folder.Folder;
import kr.minimalest.core.domain.series.Series;
import kr.minimalest.core.domain.base.BaseColumn;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseColumn {

    @Id @GeneratedValue
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

    // 해당 글이 어떤 시리즈에 해당하는지를 나타냅니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", nullable = true)
    private Series series;

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
}
