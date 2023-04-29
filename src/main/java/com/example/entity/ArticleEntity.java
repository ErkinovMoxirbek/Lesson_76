package com.example.entity;

import com.example.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "article")
@Entity
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "title", columnDefinition = "text")
    private String title;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "content", columnDefinition = "text")
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ArticleStatus status = ArticleStatus.NOT_PUBLISHED;
    @Column(name = "shared_count")
    private Integer sharedCount = 0;
    @ManyToOne
    @JoinColumn(name = "attach_id")
    private AttachEntity attach;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private RegionEntity region;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private ProfileEntity moderator;
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private ProfileEntity publisher;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;
    @Column(name = "view_count")
    private Integer viewCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private ArticleTypeEntity type;

    private String typeId;
}
