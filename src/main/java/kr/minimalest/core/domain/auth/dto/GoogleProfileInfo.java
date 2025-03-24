package kr.minimalest.core.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleProfileInfo {

    private String id;

    private String email;

    @JsonProperty("verified_email")
    private boolean verifiedEmail;

    private String name;

    private String picture;
}
