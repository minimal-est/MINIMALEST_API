package kr.minimalest.core.domain.member;

import jakarta.validation.Valid;
import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.member.dto.MemberFindResponse;
import kr.minimalest.core.domain.member.dto.MemberJoinRequest;
import kr.minimalest.core.domain.member.dto.MemberJoinResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberApi {

    private final MemberService memberService;

    @PostMapping
    public ApiResponse<?> joinMember(
            @Valid @RequestBody MemberJoinRequest memberJoinRequest
    ) {
        MemberJoinResponse memberJoinResponse = memberService.joinMember(memberJoinRequest);
        return ApiResponse.success(memberJoinResponse, "회원가입에 성공했습니다.");
    }

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
