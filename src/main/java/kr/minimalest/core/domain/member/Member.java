package kr.minimalest.core.domain.member;

import jakarta.persistence.*;
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

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String encPassword;

    @Column(nullable = false)
    private String email;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Archive> archives = new ArrayList<>();
}
