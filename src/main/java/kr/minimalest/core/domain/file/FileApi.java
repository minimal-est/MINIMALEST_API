package kr.minimalest.core.domain.file;

import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.file.dto.SimpleFileMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileApi {

    private final FileService fileService;

    @PostMapping
    public ApiResponse<?> uploadAndSave(
            @RequestPart("file") MultipartFile multipartFile
    ) {
        SimpleFileMetadata simpleFileMetadata = fileService.uploadAndSave(null, multipartFile);
        return ApiResponse.success(simpleFileMetadata);
    }

}
