package kr.minimalest.core.domain.member;

import jakarta.persistence.*;
import kr.minimalest.core.domain.auth.AuthType;
import kr.minimalest.core.domain.profile.Profile;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.base.BaseColumn;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String encPassword;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserLevel userLevel;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Archive> archives = new ArrayList<>();

    // 회원과 프로필은 라이프 사이클이 필요하므로 양방향 연관관계가 필요합니다.
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    public void setProfile(Profile profile) {
        this.profile = profile;
        profile.setMember(this);
    }

    public void addArchive(Archive archive) {
        this.archives.add(archive);
        archive.setMember(this);
    }
}
