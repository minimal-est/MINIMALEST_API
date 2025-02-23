package kr.minimalest.core.domain.post.service;

public interface Summarizer {

    String summarize(String content, int maxLength);
}
