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
    private String title;
    private String description;
    private String content;
    private String attachId;
    private Integer regionId;
    private Integer categoryId;
    private Integer typeId;

}
