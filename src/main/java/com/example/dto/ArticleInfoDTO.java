package com.example.dto;

import com.example.entity.RegionEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter @Setter
public class ArticleInfoDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private Integer sharedCount;
    private RegionEntity region;
    private LocalDateTime publishedDate;
    private Integer viewCount;
    private Integer likeCount;
}
