package kr.minimalest.core.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import kr.minimalest.core.domain.post.PostViewKey;
import kr.minimalest.core.domain.post.dto.PostCreateResponse;
import kr.minimalest.core.domain.post.dto.PostPreviewResponse;
import kr.minimalest.core.domain.post.dto.PostViewResponse;
import kr.minimalest.core.domain.post.service.PostHitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.data.domain.Slice;
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

    @Pointcut("execution(* kr.minimalest.core.domain.post.service.PostService.*(..))")
    public void postServicePointcut() {}

    @Before("postHitPointcut() && args(author, sequence)")
    public void increasePostHitCount(String author, Long sequence) {
        PostViewKey postViewKey = new PostViewKey(author, sequence);
        postHitService.incrementHitCount(postViewKey, getClientIp());
    }

    // 조회수를 Dto에 포함하여 반환하기
    @Around("postServicePointcut()")
    public Object setHitCounts(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof Slice<?> sliceResult) {
            // 결과가 복수일 경우
            sliceResult.getContent().forEach(this::setHitCount);
        } else {
            // 결과가 단일인 경우
            setHitCount(result);
        }

        return result;
    }

    @AfterReturning(pointcut = "postCreatePointcut()", returning = "result")
    public void createPostHit(PostCreateResponse result) {
        postHitService.createPostHit(new PostViewKey(result.getAuthor(), result.getSequence()));
    }

    private void setHitCount(Object postResponse) {
        if (postResponse instanceof PostViewResponse postViewResponse) {
            long hitCount = postHitService.getHitCount(new PostViewKey(postViewResponse.getAuthor(), postViewResponse.getSequence()));
            postViewResponse.setHitCount(hitCount);
        } else if (postResponse instanceof PostPreviewResponse postPreviewResponse) {
            long hitCount = postHitService.getHitCount(new PostViewKey(postPreviewResponse.getAuthor(), postPreviewResponse.getSequence()));
            postPreviewResponse.setHitCount(hitCount);
        }
    }

    private String getClientIp() {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {clientIp = request.getHeader("X-Real-IP");}
        if (clientIp == null || clientIp.isEmpty()) {clientIp = request.getRemoteAddr();}
        return clientIp;
    }
}
