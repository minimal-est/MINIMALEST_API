package kr.minimalest.core.domain.style.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StyleResponse {
    private StyleDto archiveStyle;
    private StyleDto postStyle;
}
