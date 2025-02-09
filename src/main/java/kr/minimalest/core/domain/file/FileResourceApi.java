package kr.minimalest.core.domain.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FileResource API", description = "파일 리소스 요청 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileResourceApi {

    private final FileService fileService;

    @Operation(summary = "가상 URL을 통해 실제 리소스를 요청합니다.")
    @GetMapping("/proxy")
    public ResponseEntity<?> findProxyResource(
            @RequestParam("key") String key
    ) {
        // 파일 리소스를 불러옵니다.
        StorageResource resource = fileService.findResource(key);

        return ResponseEntity.ok()
                .header("Content-Type", resource.getContentType())
                .header("Content-Disposition", "inline;")
                .body(new InputStreamResource(resource.getInputStream()));
    }
}
