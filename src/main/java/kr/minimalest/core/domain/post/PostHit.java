package kr.minimalest.core.domain.post;

import jakarta.persistence.*;
import kr.minimalest.core.domain.base.BaseColumn;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostHit extends BaseColumn {

    @EmbeddedId
    @Column(name = "post_view_key_id")
    private PostViewKey postViewKey;

    @Column(nullable = false)
    private long hit;

    public void updateHit(long hitValue) {
        this.hit = hitValue;
    }

    public static PostHit create(PostViewKey postViewKey, long initHit) {
        return new PostHit(postViewKey, initHit);
    }
}
