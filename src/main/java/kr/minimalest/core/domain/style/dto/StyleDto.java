package kr.minimalest.core.domain.style.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class StyleDto {
    Map<String, String> styles;
}
