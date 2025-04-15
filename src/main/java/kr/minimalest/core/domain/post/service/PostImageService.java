package kr.minimalest.core.domain.post.service;

import kr.minimalest.core.domain.file.File;
import kr.minimalest.core.domain.file.FileRepository;
import kr.minimalest.core.domain.file.metadata.FileMetadataSaver;
import kr.minimalest.core.domain.file.FileUtils;
import kr.minimalest.core.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Image;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final FileMetadataSaver fileMetadataSaver;
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

    private void saveExternalImage(String contentImageUrl, Post post) {
        String pureExt = FileUtils.extractExt(contentImageUrl);
        String contentType = toImageContentType(pureExt);
        String type = FileUtils.extractType(contentType);
        String key = FileUtils.keyResolver(type, pureExt);
        fileMetadataSaver.saveFileMetadataFromExternalReference(key, contentImageUrl, post);
    }

    private static String toImageContentType(String pureExt) {
        return "image/" + pureExt;
    }
}
