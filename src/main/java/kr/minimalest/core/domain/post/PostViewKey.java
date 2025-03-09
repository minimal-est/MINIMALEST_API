package kr.minimalest.core.domain.post;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PostViewKey implements Serializable {

    private String author;
    private long sequence;
}
