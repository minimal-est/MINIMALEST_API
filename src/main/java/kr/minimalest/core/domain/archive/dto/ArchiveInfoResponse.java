package kr.minimalest.core.domain.archive.dto;

import kr.minimalest.core.domain.archive.Archive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArchiveInfoResponse {

    private String author;
    private String mainTitle;
    private String subTitle;

    public static ArchiveInfoResponse fromEntity(Archive archive) {
        return ArchiveInfoResponse.builder()
                .author(archive.getAuthor())
                .mainTitle(archive.getMainTitle())
                .subTitle(archive.getSubTitle())
                .build();
    }
}
