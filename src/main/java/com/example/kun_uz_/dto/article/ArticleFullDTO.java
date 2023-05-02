package com.example.kun_uz_.dto.article;

import com.example.kun_uz_.dto.RegionDTO.RegionResponseDTO;
import com.example.kun_uz_.dto.categoryEntity.CategoryResponseDTO;
import com.example.kun_uz_.entity.*;
import com.example.kun_uz_.enums.ArticleStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleFullDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private Integer sharedCount = 0;
    private RegionResponseDTO region;
    private CategoryResponseDTO category;
    private LocalDateTime publishedDate;
    private Integer viewCount;
    private Integer likeCount;

}