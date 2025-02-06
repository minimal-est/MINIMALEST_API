package kr.minimalest.core.domain.archive.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArchiveInfoResponse {

    private String author;
    private String mainTitle;
    private String subTitle;
}
