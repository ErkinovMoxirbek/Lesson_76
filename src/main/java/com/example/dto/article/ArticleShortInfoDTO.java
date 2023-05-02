package com.example.dto.article;

import com.example.entity.AttachEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter @Setter
public class ArticleShortInfoDTO {
    private String id;
    private String title;
    private String description;
    private AttachEntity attach;
    private LocalDateTime publishedDate;
}
