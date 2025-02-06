package kr.minimalest.core.domain.series;

import jakarta.persistence.*;

@Entity
public class Series {

    @Id @GeneratedValue
    @Column(name = "series_id")
    private Long id;

    private String name;
}
