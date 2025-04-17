package kr.minimalest.core.domain.post.service;

import kr.minimalest.core.domain.file.FileService;
import kr.minimalest.core.domain.file.FileUtils;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.service.exception.ThumbnailException;
import kr.minimalest.core.infra.lambda.ThumbnailLambda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ThumbnailService {

    private final ThumbnailLambda thumbnailLambda;
    private final ImageUrlBuilder imageUrlBuilder;
    private final FileService fileService;

    public String generateThumbnailUrl(Post post, String imageUrl) {
        if (!StringUtils.hasText(imageUrl)) {
            throw new ThumbnailException("imageUrl이 빈 값 입니다!");
        }

        try {
            String key = FileUtils.extractKeyParameter(imageUrl);
            String thumbnailKey = thumbnailLambda.generateThumbnail(key);

            fileService.save(post, thumbnailKey);

            String thumbnailUrl = imageUrlBuilder.build(thumbnailKey);

            post.updateThumbnail(thumbnailUrl);

            return thumbnailUrl;
        } catch (Exception ex) {
            throw new ThumbnailException("썸네일 생성에 실패했습니다!", ex);
        }
    }
}
