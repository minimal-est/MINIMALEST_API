package kr.minimalest.core.domain.member;

import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.member.dto.MemberFindResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApi {

    private final MemberService memberService;

    @GetMapping("/member/{username}")
    public ApiResponse<?> findMember(
            @PathVariable String username
    ) {
        MemberFindResponse memberFindResponse = memberService.findMemberInfo(username);
        return ApiResponse.success(memberFindResponse);
    }
}
