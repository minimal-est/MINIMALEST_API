package kr.minimalest.core.domain.folder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.folder.Folder;
import kr.minimalest.core.domain.folder.FolderConstants;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FolderCreateRequest {

    @NotBlank
    @Size(
            min = FolderConstants.NAME_MIN_LENGTH,
            max = FolderConstants.NAME_MAX_LENGTH
    )
    String name;

    public Folder toEntity(Archive archive) {
        return Folder.builder()
                .archive(archive)
                .name(name)
                .build();
    }
}
