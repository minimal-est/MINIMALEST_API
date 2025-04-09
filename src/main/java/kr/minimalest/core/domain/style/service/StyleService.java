package kr.minimalest.core.domain.style.service;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.style.StyleContext;
import kr.minimalest.core.domain.style.entity.Style;
import kr.minimalest.core.domain.style.exception.StyleNotValidKeyException;
import kr.minimalest.core.domain.style.repository.StyleRepository;
import kr.minimalest.core.domain.style.type.StyleKeyType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StyleService {
    private final StyleRepository styleRepository;

    @Transactional
    public void createDefaultStylesFor(Archive archive) {
        Style archiveStyle = Style.defaultForArchive(archive);
        Style postStyle = Style.defaultForPost(archive);
        styleRepository.saveAll(List.of(archiveStyle, postStyle));
    }

    @Transactional
    public void putStyle(String author, StyleContext context, Map<String, String> styles) {
        Style style = styleRepository.findByAuthorAndContext(author, context)
                .orElseThrow(() -> new EntityNotFoundException("스타일이 존재하지 않습니다!"));

        styles.forEach((key, value) -> {
            if (context.isAcceptable(StyleKeyType.valueOf(key))) {
                style.putStyle(StyleKeyType.valueOf(key), value);
            } else {
                throw new StyleNotValidKeyException(key + " 키값은 존재하지 않습니다!");
            }
        });
    }
}
