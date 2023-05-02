package com.example.service;

import com.example.dto.article.ArticleDTO;
import com.example.dto.article.ArticleInfoDTO;
import com.example.dto.article.ArticleResponseDTO;
import com.example.dto.article.ArticleShortInfoDTO;
import com.example.dto.attach.AttachDTO;
import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.exp.AppBadRequestException;
import com.example.exp.ArticleNotFoundException;
import com.example.mapper.ArticleShortInfoMapper;
import com.example.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleService {
    private final CategoryService categoryService;
    private final RegionService regionService;
    private final ArticleTypeService articleTypeService;
    private final ProfileService profileService;
    private final AttachService attachService;
    private final ArticleRepository articleRepository;

    public ArticleResponseDTO create(ArticleResponseDTO dto, Integer moderId) {
        // check
        isValidDTO(dto);
        ProfileEntity moderator = profileService.get(moderId);
        RegionEntity region = regionService.get(dto.getRegionId());
        CategoryEntity category = categoryService.get(dto.getCategoryId());

        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setRegion(region);
        entity.setCategory(category);
        entity.setModerator(moderator);
        // type
        articleRepository.save(entity);
        return dto;
    }

    public void isValidDTO(ArticleResponseDTO dto) {
        if (dto.getDescription() == null) {
            throw new AppBadRequestException("invalid description");
        }
        if (dto.getTitle() == null) {
            throw new AppBadRequestException("invalid title");
        }
        // todo image check
        if (dto.getAttachId() == null) {
            throw new AppBadRequestException("invalid image");
        }
        if (dto.getContent() == null) {
            throw new AppBadRequestException("invalid content");
        }
    }

    public ArticleDTO update(ArticleDTO dto, String articleId) {
        Optional<ArticleEntity> optional = articleRepository.findById(articleId);
        if (optional.isPresent()) {
            ArticleEntity entity = optional.get();
            if (dto.getContent() != null) {
                entity.setContent(dto.getContent());
            }
            if (dto.getDescription() != null) {
                entity.setDescription(entity.getDescription());
            }
            if (dto.getTitle() != null) {
                entity.setTitle(dto.getTitle());
            }
            // TODO: image check
            if (dto.getAttachId() != null) {
                entity.setAttach(attachService.get(dto.getAttachId()));
            }
            if (dto.getCategoryId() != null) {
                entity.setCategory(categoryService.get(dto.getCategoryId()));
            }
            if (dto.getRegionId() != null) {
                entity.setRegion(regionService.get(dto.getRegionId()));
            }
            articleRepository.save(entity);
            return dto;
        }
        throw new ArticleNotFoundException("not found article");
    }

    public Boolean delete(String id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isPresent()) {
            ArticleEntity entity = optional.get();
            entity.setVisible(false);
            articleRepository.save(entity);
            return true;
        }
        throw new ArticleNotFoundException("not found article");
    }

    public ArticleDTO changeStatus(String id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isPresent()) {
            ArticleEntity entity = optional.get();
            if (entity.getStatus().equals(ArticleStatus.PUBLISHED)) {
                entity.setStatus(ArticleStatus.NOT_PUBLISHED);
            } else {
                entity.setStatus(ArticleStatus.PUBLISHED);
            }
            articleRepository.save(entity);
            return entityToDTO(entity);
        }
        throw new ArticleNotFoundException("article not found");
    }

    public List<ArticleDTO> articleShortInfo(Integer typeId) {
        ArticleTypeEntity typeEntity = articleTypeService.get(typeId);
        List<ArticleEntity> entityList = articleRepository.findAllByType(typeEntity);
        List<ArticleDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    public ArticleDTO entityToDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setCategoryId(entity.getCategory().getId());
        dto.setTitle(entity.getTitle());
        dto.setRegionId(entity.getRegion().getId());
        return dto;
    }

    public List<ArticleDTO> articleShortInfo(List<Integer> idList) {
        List<ArticleEntity> entityList = articleRepository.articleShortInfo(idList);
        List<ArticleDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    public ArticleEntity get(String id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ArticleNotFoundException("article not found");
        }
        return optional.get();
    }

    public ArticleInfoDTO articleFullInfo(String id, String lang) {
        ArticleEntity entity = get(id);
        ArticleInfoDTO dto = new ArticleInfoDTO();
        dto.setId(entity.getId());
        dto.setRegion(entity.getRegion());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setSharedCount(entity.getSharedCount());
        dto.setViewCount(entity.getViewCount());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public List<ArticleShortInfoDTO> articleShortInfo() {
        List<ArticleEntity> entityList = articleRepository.mostReadArticles();
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(toShortInfo(entity));
        }
        return dtoList;
    }

    public ArticleShortInfoDTO toShortInfo(ArticleEntity entity) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setAttach(entity.getAttach());
        dto.setId(entity.getId());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setDescription(entity.getDescription());
        dto.setTitle(entity.getTitle());
        return dto;
    }


    public List<ArticleShortInfoDTO> getRegionArticle(Integer id, int size, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ArticleEntity> entityPage = articleRepository.findByRegionId(id, pageable);
        long totalCount = entityPage.getTotalElements();
        List<ArticleEntity> entityList = entityPage.getContent();
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(toShortInfo(entity));
        }
        return dtoList;
    }

    public List<ArticleShortInfoDTO> get5CategoryArticle(Integer id) {
        CategoryEntity category = categoryService.get(id);
        List<ArticleEntity> entityList = articleRepository.get5CategoryArticle(category);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(toShortInfo(entity));
        }
        return dtoList;
    }   public Boolean changeStatus(ArticleStatus status, String id, Integer prtId) {
        ArticleEntity entity = get(id);
        if (status.equals(ArticleStatus.PUBLISHED)) {
            entity.setPublishedDate(LocalDateTime.now());
            entity.setPublisherId(prtId);
        }
        entity.setStatus(status);
        articleRepository.save(entity);
        // articleRepository.changeStatus(status, id);
        return true;
    }
    public List<ArticleShortInfoDTO> get4ArticleByTypes(Integer typeId, String articleId) {
        ArticleTypeEntity type = articleTypeService.get(typeId);
        List<ArticleEntity> entityList = articleRepository.findByTypeIdAndIdNot(type, articleId);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(toShortInfo(entity));
        }
        return dtoList;
    }
    public ArticleShortInfoDTO toArticleShortInfo(ArticleEntity entity) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setImage(attachService.getAttachLink(entity.getAttachId()));
        return dto;
    }
    public List<ArticleShortInfoDTO> getLast5ByTypeId(Integer typeId) {
        List<ArticleEntity> entityList = articleRepository.findTop5ByTypeIdAndStatusAndVisibleOrderByCreatedDateDesc(typeId,
                ArticleStatus.PUBLISHED, true);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }
    public AttachDTO getAttachLink(String attachId) {
        AttachDTO dto = new AttachDTO();
        dto.setId(attachId);
        dto.setUrl(serverHost + "/api/v1/attach/open/" + attachId);
        return dto;
    }
    public ArticleShortInfoDTO toArticleShortInfo(ArticleShortInfoMapper entity) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublished_date());
        dto.setImage(attachService.getAttachLink(entity.getAttachId()));
        return dto;
    }
}

