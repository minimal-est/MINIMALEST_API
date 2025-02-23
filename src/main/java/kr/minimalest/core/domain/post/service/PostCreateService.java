package kr.minimalest.core.domain.post.service;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.archive.ArchiveRepository;
import kr.minimalest.core.domain.archive.ArchiveService;
import kr.minimalest.core.domain.archive.dto.ArchiveInfoResponse;
import kr.minimalest.core.domain.folder.Folder;
import kr.minimalest.core.domain.folder.FolderRepository;
import kr.minimalest.core.domain.folder.FolderService;
import kr.minimalest.core.domain.folder.dto.FolderView;
import kr.minimalest.core.domain.post.*;
import kr.minimalest.core.domain.post.dto.PostCreateRequest;
import kr.minimalest.core.domain.post.dto.PostCreateResponse;
import kr.minimalest.core.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Image;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostCreateService implements PostCreator {

    private final PostRepository postRepository;
    private final ArchiveRepository archiveRepository;
    private final FolderRepository folderRepository;
    private final PostImageService postImageService;
    private final PostThumbnailService postThumbnailService;
    private final Extractor extractor;

    @Transactional
    public PostCreateResponse create(String author, String email, PostCreateRequest request) {
        // 폴더 및 아카이브 검증
        Folder folder = folderRepository.findById(request.getFolderId())
                .orElseThrow(() -> new EntityNotFoundException("폴더가 존재하지 않습니다!"));

        Archive archive = archiveRepository.findByAuthor(author)
                .orElseThrow(() -> new EntityNotFoundException("아카이브가 존재하지 않습니다!"));

        // 아카이브의 Post Sequence 더하기
        long nextSequence = postRepository.findMaxSequenceByArchive(author) + 1;

        // 이미지 추출 후 썸네일 선정
        List<Image> images = extractor.extract(request.getContent(), Image.class);
        Optional<Image> optionalImage = PostImageService.extractFirstImage(images);
        boolean hasThumbnail = optionalImage.isPresent();

        Post post = request.toEntity(folder, archive, nextSequence, hasThumbnail, null);

        // 썸네일 저장 및 업로드 후 URL 생성
        String thumbnailUrl = optionalImage.map(image -> postThumbnailService.createThumbnailUrl(image, post)).orElse(null);

        // 포스트에 썸네일 URL 부착
        post.setThumbnailUrl(thumbnailUrl);

        // 포스트 저장
        postRepository.save(post);

        // 내부 이미지를 업로드 및 포스트에 부착
        postImageService.processSaveImages(images, post);

        return new PostCreateResponse(author, nextSequence);
    }
}
