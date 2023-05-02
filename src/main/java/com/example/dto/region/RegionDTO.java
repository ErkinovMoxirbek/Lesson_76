package com.example.dto.region;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class RegionDTO {
    private Integer id;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private LocalDateTime createdDate;
    private Boolean visible;
}
