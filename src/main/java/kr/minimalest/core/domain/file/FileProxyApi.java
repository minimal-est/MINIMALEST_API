package kr.minimalest.core.domain.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileProxyApi {

    private final FileService fileService;

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
