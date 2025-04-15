package kr.minimalest.core.domain.file;

import kr.minimalest.core.domain.file.storage.StorageResource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileResourceApi {

    private final FileService fileService;

    @GetMapping("/proxy")
    public ResponseEntity<?> findProxyResource(
            @RequestParam("key") String key
    ) {
        // 파일 리소스를 불러옵니다.
        StorageResource resource = fileService.findStorageResource(key);

        return ResponseEntity.ok()
                .header("Content-Type", resource.getContentType())
                .header("Content-Disposition", "inline;")
                .body(new InputStreamResource(resource.getInputStream()));
    }
}
