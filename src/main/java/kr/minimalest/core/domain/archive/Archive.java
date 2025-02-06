package kr.minimalest.core.domain.archive;

import jakarta.persistence.*;
import kr.minimalest.core.domain.base.BaseColumn;
import kr.minimalest.core.domain.member.Member;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Archive extends BaseColumn {

    @Id
    @GeneratedValue
    @Column(name = "archive_id")
    private Long id;

    // 아카이브 작가명
    @Column(unique = true, nullable = false)
    private String author;

    @Column(nullable = false)
    private String mainTitle;

    private String subTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        member.getArchives().add(this);
    }
}
