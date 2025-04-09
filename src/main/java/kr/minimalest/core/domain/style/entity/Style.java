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
