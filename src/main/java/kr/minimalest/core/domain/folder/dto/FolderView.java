package kr.minimalest.core.domain.folder.dto;

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
}
