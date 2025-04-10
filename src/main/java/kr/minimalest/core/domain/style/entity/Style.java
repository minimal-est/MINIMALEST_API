package kr.minimalest.core.domain.style.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.base.BaseColumn;
import kr.minimalest.core.domain.style.Stylable;
import kr.minimalest.core.domain.style.StyleContext;
import kr.minimalest.core.domain.style.type.StyleKeyType;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(
        name = "style",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"archive_id", "domain_type"})
        }
)
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Style extends BaseColumn implements Stylable {

    @Column(name = "style_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_id", nullable = false)
    private Archive archive;

    @Column(name = "domain_type")
    @Enumerated(EnumType.STRING)
    private StyleContext domainType;

    @Type(value = JsonType.class)
    @Column(columnDefinition = "JSON")
    private Map<String, String> styles = new HashMap<>();

    @Override
    public void putStyle(StyleKeyType key, String value) {
        if (context().isAcceptable(key)) {
            this.styles.put(key.value(), value);
        }
    }

    @Override
    public StyleContext context() {
        return this.domainType;
    }

    /**
     * StyleContext에 해당하는 속성으로 스타일을 가져옵니다.
     * @param archive 대상 아카이브
     * @param context 속성 집합(ex 아카이브, 포스트..)
     * @return 스타일 정보 ex {backgroundColor, "#FF00FF"}
     */
    public static Map<String, String> getStyle(Archive archive, StyleContext context) {
        return archive.getStyles()
                .stream()
                .filter(style -> style.getDomainType() == context)
                .findFirst()
                .map(Style::getStyles)
                .orElseThrow(() -> new EntityNotFoundException(context + "해당 스타일이 존재하지 않습니다!"));
    }

    /**
     * 아카이브 생성과 동시에 스타일 엔티티 생성 시, 초기값을 매핑할 수 있도록 지원하는 편의성 메소드입니다.
     * @param archive 아카이브 정보
     * @return 스타일 엔티티
     */

    public static Style defaultForArchive(Archive archive) {
        Map<String, String> styles = new HashMap<>();
        styles.put(StyleKeyType.BACKGROUND_COLOR.value(), "#FFFFFF");
        styles.put(StyleKeyType.FONT_COLOR.value(), "#000000");

        return Style.builder()
                .archive(archive)
                .domainType(StyleContext.ARCHIVE)
                .styles(styles)
                .build();
    }

    /**
     * 아카이브 생성과 동시에 스타일 엔티티 생성 시, 초기값을 매핑할 수 있도록 지원하는 편의성 메소드입니다.
     * @param archive 아카이브 정보
     * @return 스타일 엔티티
     */

    public static Style defaultForPost(Archive archive) {
        Map<String, String> styles = new HashMap<>();
        styles.put(StyleKeyType.BACKGROUND_COLOR.value(), "#fbfff0");

        return Style.builder()
                .archive(archive)
                .domainType(StyleContext.POST)
                .styles(styles)
                .build();
    }
}
