package kr.minimalest.core.domain.post.service;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.archive.ArchiveRepository;
import kr.minimalest.core.domain.folder.Folder;
import kr.minimalest.core.domain.folder.FolderRepository;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.dto.PostCreateRequest;
import kr.minimalest.core.domain.post.dto.PostCreateResponse;
import kr.minimalest.core.domain.post.repository.PostRepository;
import org.commonmark.node.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("포스트 생성 서비스 테스트")
class PostCreateServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ArchiveRepository archiveRepository;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private PostImageService postImageService;

    @Mock
    private ThumbnailService thumbnailService;

    @Mock
    private Extractor extractor;

    @InjectMocks
    private PostCreateService postCreateService;

    private static PostCreateRequest createFullRequest() {
        final PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title("test title")
                .content("test content")
                .thumbnailUrl("https://sample.kr/?key=thumbnail/image/sample.png")
                .folderId(1L)
                .build();
        return postCreateRequest;
    }

    /**
     * create
     */

    @Test
    @DisplayName("포스트 생성 성공")
    void createPost_success() {
        // given
        final Folder folder = mock(Folder.class);
        final Archive archive = mock(Archive.class);
        final Post post = mock(Post.class);
        final List<Image> images = List.of(new Image());

        final PostCreateRequest postCreateRequest = createFullRequest();

        when(folderRepository.findById(anyLong())).thenReturn(Optional.of(folder));
        when(archiveRepository.findByAuthor(anyString())).thenReturn(Optional.of(archive));
        when(postRepository.findMaxSequenceByArchive(anyString())).thenReturn(5L);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(extractor.extract(anyString(), eq(Image.class))).thenReturn(images);

        // when
        PostCreateResponse postCreateResponse = postCreateService.create("testAuthor", "test@email.com", postCreateRequest);

        // then
        assertThat(postCreateResponse.getAuthor()).isEqualTo("testAuthor");
        assertThat(postCreateResponse.getSequence()).isEqualTo(6L);
    }

    @Test
    @DisplayName("썸네일이 없는 경우 썸네일 생성 서비스 호출 안 함")
    void createPost_withoutThumbnail_success() {
        // given
        final Folder folder = mock(Folder.class);
        final Archive archive = mock(Archive.class);
        final Post post = mock(Post.class);
        final List<Image> images = List.of(new Image());

        PostCreateRequest postCreateRequest = createFullRequest();
        postCreateRequest.setThumbnailUrl(null);

        when(folderRepository.findById(anyLong())).thenReturn(Optional.of(folder));
        when(archiveRepository.findByAuthor(anyString())).thenReturn(Optional.of(archive));
        when(postRepository.findMaxSequenceByArchive(anyString())).thenReturn(5L);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(extractor.extract(anyString(), eq(Image.class))).thenReturn(images);

        // when
        postCreateService.create("testAuthor", "test@email.com", postCreateRequest);

        // then
        verify(thumbnailService, never()).generateThumbnailUrl(any(), any());
    }

    @Test
    @DisplayName("포스트 생성 중 폴더가 존재하지 않는 예외 발생")
    void createPost_folderNotValid_throwsException() {
        // given
        final PostCreateRequest postCreateRequest = createFullRequest();

        when(folderRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            postCreateService.create("testAuthor", "test@email.com", postCreateRequest);
        });

        assertThat(ex.getMessage()).isEqualTo("폴더가 존재하지 않습니다!");
    }

    @Test
    @DisplayName("포스트 생성 중 아카이브가 존재하지 않는 예외 발생")
    void createPost_archiveNotValid_throwsException() {
        // given
        Folder folder = mock(Folder.class);

        final PostCreateRequest postCreateRequest = createFullRequest();

        when(folderRepository.findById(1L)).thenReturn(Optional.of(folder));
        when(archiveRepository.findByAuthor("testAuthor")).thenReturn(Optional.empty());

        // when & then
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            postCreateService.create("testAuthor", "test@email.com", postCreateRequest);
        });

        assertThat(ex.getMessage()).isEqualTo("아카이브가 존재하지 않습니다!");
    }

    @Test
    @DisplayName("첫 포스트 생성 시 시퀀스가 1로 설정되는지 확인")
    void createPost_firstPostSequence_shouldBe1() {
        // given
        final Folder folder = mock(Folder.class);
        final Archive archive = mock(Archive.class);
        final Post post = mock(Post.class);
        final List<Image> images = List.of(new Image());

        when(folderRepository.findById(anyLong())).thenReturn(Optional.of(folder));
        when(archiveRepository.findByAuthor(anyString())).thenReturn(Optional.of(archive));
        when(postRepository.findMaxSequenceByArchive(anyString())).thenReturn(0L);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(extractor.extract(anyString(), eq(Image.class))).thenReturn(images);

        // when
        PostCreateResponse postCreateResponse = postCreateService.create("testAuthor", "test@email.com", createFullRequest());

        // then
        assertThat(postCreateResponse.getSequence()).isEqualTo(1L);
    }
}
