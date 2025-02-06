package kr.minimalest.core.domain.file;

import jakarta.persistence.*;
import kr.minimalest.core.domain.base.BaseColumn;
import kr.minimalest.core.domain.post.Post;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class File extends BaseColumn {

    @Id @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    // 원본 파일명
    @Column(nullable = false)
    private String filename;

    // 클라이언트에서 파일을 요청할 때 사용하는 UUID 값으로, 고유합니다.
    @Column(nullable = false)
    private String storageKey;

    // 클라이언트에 제공할 파일 주소값입니다.
    @Column(nullable = false, unique = true)
    private String virtualUrl;

    // 저장소 타입을 나타냅니다.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileStorageType fileStorageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @Column(nullable = true)
    private String description;

    public void attachPost(Post post) {
        this.post = post;
    }
}
