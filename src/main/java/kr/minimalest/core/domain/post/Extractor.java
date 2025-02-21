package kr.minimalest.core.domain.post;

import java.util.List;

public interface Extractor {

    <T>List<T> extract(String target, Class<T> type);

    String extractPureText(String target);
}
