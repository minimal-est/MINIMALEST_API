package kr.minimalest.core.domain.file;

import kr.minimalest.core.domain.file.storage.StorageResource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileResourceApi {

    @Value("${spring.cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;

    private final FileService fileService;

    @Deprecated
    @GetMapping("/proxy/direct")
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

    @GetMapping("/proxy")
    public ResponseEntity<?> getCloudFrontResource(
            @RequestParam("key") String key
    ) {
        String cloudFrontResourceUrl = cloudFrontDomain + "/" + key;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(cloudFrontResourceUrl));

        return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
    }
}
