package kr.minimalest.core.domain.folder.dto;

import kr.minimalest.core.domain.folder.Folder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FolderView {

    private Long id;
    private String name;
    private int depth;
    private Long parentId;
    private List<FolderView> children;
    private boolean isRoot;
    private boolean isLeaf;

    // dto로 반환되는 posts는 비어있습니다.
    // 만약, 해당 폴더에 맞는 포스트들을 포함하려면 추가 작업이 필요합니다.
    private List<FolderWithPost> posts;

    public FolderView(Long id, String name, int depth, Long parentId, boolean isRoot, boolean isLeaf) {
        this.id = id;
        this.name = name;
        this.depth = depth;
        this.parentId = parentId;
        this.isRoot = isRoot;
        this.isLeaf = isLeaf;

        this.children = new ArrayList<>();
        this.posts = new ArrayList<>();
    }

    public static FolderView fromEntity(Folder folder) {
        return new FolderView(
                folder.getId(),
                folder.getName(),
                folder.getDepth(),
                folder.getParent() == null ? null : folder.getParent().getId(),
                folder.isRoot(),
                folder.isLeaf()
        );
    }
}
