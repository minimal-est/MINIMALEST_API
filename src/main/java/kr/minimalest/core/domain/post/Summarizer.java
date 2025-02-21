package kr.minimalest.core.domain.post;

public interface Summarizer {

    String summarize(String content, int maxLength);
}
