package kr.minimalest.core.domain.member;

import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.member.dto.MemberFindResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        MemberFindResponse memberFindResponse = memberService.findAndValidate(email, author);
        return ApiResponse.success(memberFindResponse);
    }

    @GetMapping("/{username}")
    public ApiResponse<?> findMember(
            @PathVariable String username
    ) {
        MemberFindResponse memberFindResponse = memberService.findAndValidate(username);
        return ApiResponse.success(memberFindResponse);
    }
}
