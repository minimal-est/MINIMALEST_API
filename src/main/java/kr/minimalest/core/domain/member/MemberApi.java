package kr.minimalest.core.domain.member;

import jakarta.validation.Valid;
import kr.minimalest.core.common.annotation.Authenticate;
import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.archive.ArchiveService;
import kr.minimalest.core.domain.archive.dto.ArchiveInfoResponses;
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
    private final ArchiveService archiveService;

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

    @GetMapping("/{email}")
    public ApiResponse<?> findMember(
            @PathVariable String email
    ) {
        MemberFindResponse memberFindResponse = memberService.findAndValidate(email);
        return ApiResponse.success(memberFindResponse);
    }

    @Authenticate
    @GetMapping("/{email}/archive")
    public ApiResponse<?> findArchives(
            @PathVariable String email
    ) {
        ArchiveInfoResponses archiveInfoResponses = archiveService.findArchiveInfos(email);
        return ApiResponse.success(archiveInfoResponses);
    }
}
