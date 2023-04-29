package com.example.dto;

import com.example.entity.AttachEntity;

import java.time.LocalDate;

public class ArticleInfoDTO {
    private String id;
    private String title;
    private String description;
    private LocalDate publishedDate;
    private AttachEntity attach;
}
