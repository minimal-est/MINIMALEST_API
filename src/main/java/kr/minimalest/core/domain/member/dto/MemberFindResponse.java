package kr.minimalest.core.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberFindResponse {

    private String username;
    private String email;
}
