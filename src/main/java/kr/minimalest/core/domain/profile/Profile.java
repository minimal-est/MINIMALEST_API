package kr.minimalest.core.domain.profile;

import jakarta.persistence.*;
import kr.minimalest.core.domain.base.BaseColumn;
import kr.minimalest.core.domain.member.Member;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Profile extends BaseColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = true)
    private String profileImageUrl;

    public Profile(
            String profileImageUrl
    ) {
        this.profileImageUrl = profileImageUrl;
    }
}
