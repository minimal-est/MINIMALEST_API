package kr.minimalest.core.domain.archive.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kr.minimalest.core.common.utils.RegexUtils;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.folder.FolderConstants;
import kr.minimalest.core.domain.member.Member;
import lombok.Data;

import static kr.minimalest.core.domain.archive.ArchiveConstants.*;

@Data
public class ArchiveCreateRequest {

    @NotBlank
    @Pattern(
            regexp = RegexUtils.NO_SPECIAL_CHAR_PATTERN,
            message = "작가 이름에는 특수문자가 포함될 수 없습니다."
    )
    @Size(
            min = AUTHOR_MIN_LENGTH,
            max = AUTHOR_MAX_LENGTH
    )
    private String author;

    @NotBlank
    @Size(max = MAIN_TITLE_MAX_LENGTH)
    private String mainTitle;

    @Size(max = SUB_TITLE_MAX_LENGTH)
    private String subTitle;

    @NotBlank
    @Size(
            min = FolderConstants.NAME_MIN_LENGTH,
            max = FolderConstants.NAME_MAX_LENGTH
    )
    private String firstFolderName;

    public Archive toEntity(Member member) {
        return Archive.builder()
                .member(member)
                .author(author)
                .mainTitle(mainTitle)
                .subTitle(subTitle)
                .build();
    }
}
