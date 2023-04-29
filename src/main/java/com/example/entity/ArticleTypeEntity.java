package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "article_type")
@Getter @Setter
public class ArticleTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "key")
    private String key;
    @Column(name = "name_uz")
    private String nameUz;
    @Column(name = "name_ru")
    private String nameRu;
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "visible")
    private Boolean visible;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    private Integer prtId;
}
