package com.example.dto.article;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ArticleTypeDTO {
    private Integer id;
    private String nameUz;
    private String nameEn;
    private String nameRu;
    private LocalDateTime createdDate;
    private Boolean visible;
}
