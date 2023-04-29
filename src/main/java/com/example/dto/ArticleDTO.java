package com.example.dto;

import com.example.entity.CategoryEntity;
import com.example.entity.RegionEntity;
import com.example.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
public class ArticleDTO {
    private Integer id;
    private String title;
    private String description;
    private String content;
    private Long sharedCount;
    private Integer imageId;
    private Integer regionId;
    private RegionEntity region;
    private Integer categoryId;
    private CategoryEntity category;
    private Integer moderatorId;
    private Integer publisherId;
    private ArticleStatus status;
    private LocalDateTime created_date;
    private LocalDate published_date;
    private Boolean visible;
    private Long view_count;

}
