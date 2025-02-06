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

    // 부제목으로, 선택사항입니다.
    private String subtitle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // 작성 순서대로 부여되는 번호입니다.
    @Column(nullable = false)
    private Long sequence;

    // 해당 글이 어떤 시리즈에 해당하는지를 나타냅니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_id")
    private Archive archive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    private boolean hasThumbnail;

    @Setter
    private String thumbnailUrl;
}
