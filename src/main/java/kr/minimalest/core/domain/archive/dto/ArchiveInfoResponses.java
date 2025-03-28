package kr.minimalest.core.domain.archive.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ArchiveInfoResponses {

    int count;

    @JsonProperty(value = "isEmpty")
    boolean isEmpty;

    List<ArchiveInfoResponse> archiveInfoResponses;

    public ArchiveInfoResponses(List<ArchiveInfoResponse> archiveInfoResponses) {
        this.count = archiveInfoResponses.size();
        this.isEmpty = archiveInfoResponses.isEmpty();
        this.archiveInfoResponses = archiveInfoResponses;
    }
}
