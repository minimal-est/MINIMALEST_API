package kr.minimalest.core.domain.post.service;

import kr.minimalest.core.domain.file.File;
import kr.minimalest.core.domain.file.FileRepository;
import kr.minimalest.core.domain.file.FileStorageService;
import kr.minimalest.core.domain.file.FileUtils;
import kr.minimalest.core.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Image;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final FileStorageService fileStorageService;
    private final FileRepository fileRepository;

    @Transactional(readOnly = true)
    public void processSaveImages(List<Image> contentImages, Post post) {
        List<String> contentImageUrls = contentImages.stream().map(Image::getDestination).toList();
        List<File> savedImageFiles = fileRepository.findAllByVirtualUrls(contentImageUrls);
        List<String> savedImageUrls = savedImageFiles.stream().map(File::getVirtualUrl).toList();

        // 내부 이미지 Post 부착
        for (File savedImageFile : savedImageFiles) {
            savedImageFile.attachPost(post);
        }

        // 외부에서 부착한 이미지라면 지금 저장 (DB에 없기 때문에)
        for (Image contentImage : contentImages) {
            String contentImageUrl = contentImage.getDestination();
            if (!savedImageUrls.contains(contentImage.getDestination())) {
                saveExternalImage(contentImageUrl, post);
            }
        }
    }

    // 본문에서 첫 이미지(마크다운)를 추출합니다.
    public static Optional<Image> extractFirstImage(List<Image> images) {
        return images.isEmpty() ? Optional.empty() : Optional.of(images.get(0));
    }

    private void saveExternalImage(String contentImageUrl, Post post) {
        String pureExt = FileUtils.extractExt(contentImageUrl);
        String contentType = toImageContentType(pureExt);
        String type = FileUtils.extractType(contentType);
        String key = FileUtils.keyResolver(type, pureExt);
        fileStorageService.saveMetadataFromExternal(key, contentImageUrl, post);
    }

    private static String toImageContentType(String pureExt) {
        return "image/" + pureExt;
    }
}
