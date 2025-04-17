package kr.minimalest.core.domain.post.service;

import kr.minimalest.core.domain.file.File;
import kr.minimalest.core.domain.file.FileRepository;
import kr.minimalest.core.domain.file.metadata.FileMetadataSaver;
import kr.minimalest.core.domain.post.ContentImage;
import kr.minimalest.core.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final FileMetadataSaver fileMetadataSaver;
    private final FileRepository fileRepository;

    @Transactional(readOnly = true)
    public void saveImageMetadataAndAttachToPost(List<ContentImage> contentImages, Post post) {
        List<File> savedImageFiles = loadAndAttachSavedImageFiles(contentImages, post);

        saveOnlyNewImages(contentImages, post, savedImageFiles);
    }

    private List<File> loadAndAttachSavedImageFiles(List<ContentImage> contentImages, Post post) {
        List<File> savedImageFiles = this.findAlreadySavedFiles(contentImages);
        attachPostToFiles(post, savedImageFiles);
        return savedImageFiles;
    }

    private List<File> findAlreadySavedFiles(List<ContentImage> contentImages) {
        List<String> contentImageUrls = extractImageUrlsFromImageNodes(contentImages);
        return findAlreadySavedFilesFromUrls(contentImageUrls);
    }

    private static List<String> extractImageUrlsFromImageNodes(List<ContentImage> contentImages) {
        return contentImages.stream().map(ContentImage::getUrl).toList();
    }

    private List<File> findAlreadySavedFilesFromUrls(List<String> contentImageUrls) {
        return fileRepository.findAllByVirtualUrls(contentImageUrls);
    }

    private static void attachPostToFiles(Post post, List<File> savedImageFiles) {
        for (File savedImageFile : savedImageFiles) {
            savedImageFile.attachPost(post);
        }
    }

    private void saveOnlyNewImages(List<ContentImage> contentImages, Post post, List<File> savedImageFiles) {
        List<String> savedImageUrls = extractImageUrls(savedImageFiles);
        saveNewImages(contentImages, post, savedImageUrls);
    }

    private static List<String> extractImageUrls(List<File> savedImageFiles) {
        return savedImageFiles.stream().map(File::getVirtualUrl).toList();
    }

    private void saveNewImages(List<ContentImage> contentImages, Post post, List<String> savedImageUrls) {
        contentImages.stream()
                .filter(image -> isNotAlreadySaved(image, savedImageUrls))
                .forEach(image -> saveExternalImageAndAttachPost(image, post));
    }

    private static boolean isNotAlreadySaved(ContentImage contentImage, List<String> savedImageUrls) {
        return !contentImage.isContained(savedImageUrls);
    }

    private void saveExternalImageAndAttachPost(ContentImage contentImage, Post post) {
        String key = contentImage.resolveKey();
        fileMetadataSaver.saveFileMetadataFromExternalReference(key, contentImage.getUrl(), post);
    }
}
