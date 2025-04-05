package kr.minimalest.core.domain.post.service;

import kr.minimalest.core.domain.file.FileService;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.service.exception.ThumbnailException;
import kr.minimalest.core.infra.lambda.ThumbnailLambda;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("썸네일 서비스 테스트")
class ThumbnailServiceTest {

    @Mock
    private ThumbnailLambda thumbnailLambda;

    @Mock
    private ImageUrlBuilder imageUrlBuilder;

    @Mock
    private FileService fileService;

    @InjectMocks
    private ThumbnailService thumbnailService;

    @Test
    @DisplayName("썸네일 URL 생성 성공")
    void generateThumbnailUrl_success() {
        // given
        final Post post = mock(Post.class);

        final String imageKey = "image/sample.png";
        final String generatedThumbnailKey = "thumbnail/" + imageKey;

        final String imageUrl = "https://sample.kr/?key=" + imageKey;
        final String thumbnailUrl = "https://sample.kr/?key=" + generatedThumbnailKey;

        when(thumbnailLambda.generateThumbnail(imageKey)).thenReturn(generatedThumbnailKey);
        when(imageUrlBuilder.build(generatedThumbnailKey)).thenReturn(thumbnailUrl);

        // when
        String result = thumbnailService.generateThumbnailUrl(post, imageUrl);

        // then
        assertThat(result).isEqualTo(thumbnailUrl);

        verify(thumbnailLambda).generateThumbnail(imageKey);
        verify(fileService).save(post, generatedThumbnailKey);
        verify(imageUrlBuilder).build(generatedThumbnailKey);
        verify(post).updateThumbnailUrl(thumbnailUrl);
    }

    @Test
    @DisplayName("썸네일로 사용할 imageUrl이 빈 값일 때 예외 발생")
    void generateThumbnailUrl_imageUrlEmpty_throwsException() {
        // given
        final Post post = mock(Post.class);
        final String imageUrl = "";

        // when & then
        ThumbnailException ex = assertThrows(ThumbnailException.class, () -> {
            thumbnailService.generateThumbnailUrl(post, imageUrl);
        });

        assertThat(ex.getMessage()).isEqualTo("imageUrl이 빈 값 입니다!");
    }

    @Test
    @DisplayName("썸네일 생성 실패 예외 발생")
    void generateThumbnailUrl_lambdaFail_throwsException() {
        // given
        final Post post = mock(Post.class);
        final String imageKey = "image/sample.png";
        final String imageUrl = "https://sample.kr?key=image/sample.png";

        when(thumbnailLambda.generateThumbnail(imageKey)).thenThrow(new RuntimeException("람다 함수 실패"));

        // when & then
        ThumbnailException ex = assertThrows(ThumbnailException.class, () -> {
            thumbnailService.generateThumbnailUrl(post, imageUrl);
        });

        assertThat(ex.getMessage()).isEqualTo("썸네일 생성에 실패했습니다!");
    }
}
