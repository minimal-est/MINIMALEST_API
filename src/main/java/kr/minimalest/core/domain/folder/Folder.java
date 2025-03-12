package kr.minimalest.core.domain.folder;

import jakarta.persistence.*;
import kr.minimalest.core.domain.archive.Archive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    private Folder parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_id")
    private Archive archive;

    @Builder
    public Folder(String name, @Nullable Folder parent, Archive archive) {
        this.name = name;
        this.archive = archive;
        setParent(parent);
    }

    public boolean isLeaf() {
        return parent == null;
    }

    public boolean isRoot() {
        return depth == 1;
    }

    public void setParent(Folder newParent) {
        if (this.parent != null) {
            // 기존 부모와의 관계 제거
            this.parent.removeChild(this);
        }

        this.parent = newParent;

        if (newParent == null) {
            this.depth = 1;
        } else {
            this.depth = newParent.depth + 1;
            newParent.addChild(this);
        }
    }

    private void addChild(Folder child) {
        if (!this.children.contains(child)) {
            this.children.add(child);
        }
    }

    private void removeChild(Folder child) {
        this.children.remove(child);
    }
}
