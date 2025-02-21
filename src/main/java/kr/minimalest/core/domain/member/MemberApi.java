package kr.minimalest.core.domain.member;

import kr.minimalest.core.common.annotation.Authenticate;
import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.member.dto.MemberFindResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberApi {

    private final MemberService memberService;

    @GetMapping("/{email}/archive/{author}")
    public ApiResponse<?> findMember(
            @PathVariable String email,
            @PathVariable String author
    ) {
        MemberFindResponse memberFindResponse = memberService.findMemberInfo(email, author);
        return ApiResponse.success(memberFindResponse);
    }

    @GetMapping("/{username}")
    public ApiResponse<?> findMember(
            @PathVariable String username
    ) {
        MemberFindResponse memberFindResponse = memberService.findMemberInfo(username);
        return ApiResponse.success(memberFindResponse);
    }

    @Authenticate
    @GetMapping("/test")
    public ApiResponse<?> login() {
        log.info("컨트롤러 시도");
        return ApiResponse.success();
    }
}
