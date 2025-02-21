package kr.minimalest.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig implements PageableHandlerMethodArgumentResolverCustomizer {

    private final static int DEFAULT_PAGE_NUMBER = 0;
    private final static int DEFAULT_PAGE_SIZE = 5;
    private final static int DEFAULT_MAX_PAGE_SIZE = 20;
    private final static Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;
    private final static String DEFAULT_SORT_PROPERTY = "createdAt";

    @Override
    public void customize(PageableHandlerMethodArgumentResolver pageableResolver) {
        pageableResolver.setFallbackPageable(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_DIRECTION,
                DEFAULT_SORT_PROPERTY)));
        pageableResolver.setMaxPageSize(DEFAULT_MAX_PAGE_SIZE);
    }
}
