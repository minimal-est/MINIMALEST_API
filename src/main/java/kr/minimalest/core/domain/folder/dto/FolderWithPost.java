package kr.minimalest.core.domain.folder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FolderWithPost {

    private Long postId;
    private String title;
}
