package kr.minimalest.core.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import kr.minimalest.core.domain.post.PostViewKey;
import kr.minimalest.core.domain.post.dto.PostCreateResponse;
import kr.minimalest.core.domain.post.service.PostHitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PostHitAspect {

    private final PostHitService postHitService;
    private final HttpServletRequest request;

    @Pointcut("@annotation(kr.minimalest.core.common.annotation.PostHitCount)")
    public void postHitPointcut() {}

    @Pointcut("execution(* kr.minimalest.core.domain.post.service.PostService.create(..))")
    public void postCreatePointcut() {}

    @AfterReturning(pointcut = "postCreatePointcut()", returning = "result")
    public void createPostHit(PostCreateResponse result) {
        postHitService.createPostHit(new PostViewKey(result.getAuthor(), result.getSequence()));
    }

    @Before("postHitPointcut() && args(author, sequence)")
    public void increasePostHitCount(String author, Long sequence) {
        PostViewKey postViewKey = new PostViewKey(author, sequence);
        postHitService.incrementHitCount(postViewKey, getClientIp());
        log.info("Post: {}, 총조회수 : {}, IP : {}", postViewKey, postHitService.getHitCount(postViewKey), getClientIp());
    }

    private String getClientIp() {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }
}
