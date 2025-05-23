package kr.minimalest.core.domain.archive;

import jakarta.persistence.*;
import kr.minimalest.core.domain.base.BaseColumn;
import kr.minimalest.core.domain.member.Member;
import kr.minimalest.core.domain.style.entity.Style;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Archive extends BaseColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "archive_id")
    private Long id;

    // 아카이브 작가명
    @Column(unique = true, nullable = false)
    private String author;

    @Column(nullable = false)
    private String mainTitle;

    @Column(nullable = true)
    private String subTitle;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "archive")
    private List<Style> styles;
}
