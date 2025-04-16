package kr.minimalest.core.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleFileMetadata {

    private String filename;
    private String virtualUrl;
    private String key;
}
