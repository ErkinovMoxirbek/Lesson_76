package com.example.kun_uz_.service;

import com.example.kun_uz_.dto.RegionDTO.RegionResponseDTO;
import com.example.kun_uz_.dto.article.ArticleFullDTO;
import com.example.kun_uz_.dto.article.ArticleRequestDTO;
import com.example.kun_uz_.dto.article.ArticleShortInfoDTO;
import com.example.kun_uz_.dto.categoryEntity.CategoryResponseDTO;
import com.example.kun_uz_.entity.*;
import com.example.kun_uz_.enums.ArticleStatus;
import com.example.kun_uz_.exps.ItemNotFoundException;
import com.example.kun_uz_.exps.MethodNotAllowedException;
import com.example.kun_uz_.mapper.ArticleShortInfoMapper;
import com.example.kun_uz_.repository.ArticleRepository;
import com.example.kun_uz_.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;
    private final RegionService regionService;
    private final CategoryService categoryService;
    private final AttachService attachService;

    public ArticleRequestDTO create(ArticleRequestDTO dto, Integer moderId) {
        // check
//        ProfileEntity moderator = profileService.get(moderId);
//        RegionEntity region = regionService.get(dto.getRegionId());
//        CategoryEntity category = categoryService.get(dto.getCategoryId());
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setModeratorId(moderId);
        entity.setRegionId(dto.getRegionId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setAttachId(dto.getAttachId());
        entity.setTypeId(dto.getTypeId());
        articleRepository.save(entity);
        return dto;
    }

    public ArticleRequestDTO update(ArticleRequestDTO dto, String id) {
        ArticleEntity entity = get(id);
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setRegionId(dto.getRegionId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setAttachId(dto.getAttachId());
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        articleRepository.save(entity);
        return dto;
    }

    public boolean delete(String id) {
        ArticleEntity entity = articleRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new RuntimeException("this article is null");
        }
        entity.setVisible(false);
        articleRepository.save(entity);
        return true;
    }

    public Boolean changeStatus(ArticleStatus status, String id, Integer prtId) {
        ArticleEntity entity = get(id);
        if (status.equals(ArticleStatus.NOT_PUBLISHED)) {
            entity.setPublishedDate(LocalDateTime.now());
            entity.setPublisherId(prtId);
        }
        entity.setStatus(status);
        articleRepository.save(entity);
        articleRepository.changeStatus(status, id);
        return true;
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

    public List<ArticleShortInfoDTO> getLast5ByTypeId2(Integer typeId) {
        List<ArticleEntity> entityList = articleRepository.find5ByTypeId(typeId,
                ArticleStatus.PUBLISHED);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }

    public List<ArticleShortInfoDTO> getLast3ByTypeId(Integer typeId) {
        List<ArticleEntity> entityList = articleRepository.findTop3ByTypeIdAndStatusAndVisibleOrderByCreatedDateDesc(typeId,
                ArticleStatus.PUBLISHED, true);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }


    public ArticleEntity DTOToEntity(ArticleRequestDTO dto) {
        ArticleEntity entity = new ArticleEntity();
        entity.setContent(dto.getContent());
        entity.setCategory(categoryRepository.findById(dto.getCategoryId()).orElse(null));
        entity.setDescription(dto.getDescription());
//        entity.setSharedCount(dto.getSharedCount());
        return entity;
    }


    public ArticleEntity get(String id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Item not found: " + id);
        }
        return optional.get();
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

    public ArticleShortInfoDTO toArticleShortInfo(ArticleShortInfoMapper entity) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublished_date());
        dto.setImage(attachService.getAttachLink(entity.getAttachId()));
        return dto;
    }

    public List<ArticleShortInfoDTO> getLast8WithoutList(List<String> list1) {
        List<ArticleEntity> entityList = articleRepository.getAllArticle(ArticleStatus.NOT_PUBLISHED);
        List<ArticleEntity> list = new LinkedList<>();
        List<ArticleShortInfoDTO> infoDTOS = new LinkedList<>();
        for (String str : list1) {
            for (ArticleEntity articleEntity : entityList) {
                if (!str.equals(articleEntity.getId())) {
                    list.add(articleEntity);
                } else {
                    break;
                }
                if (list.size() == 8) break;
            }
            if (list.size() == 8) break;
        }
        list.forEach(entity -> {
            infoDTOS.add(toArticleShortInfo(entity));
        });
        return infoDTOS;
    }


    public ArticleFullDTO getByIdAndLanguage(String id, String language) {
        Optional<ArticleEntity> optional = articleRepository.getById(id, ArticleStatus.NOT_PUBLISHED);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Article not found");
        }
        ArticleEntity entity = optional.get();
        switch (language) {
            case "uz" -> {
                RegionEntity region = regionService.get(entity.getRegionId());
                RegionResponseDTO regionResponseDTO = new RegionResponseDTO();
                regionResponseDTO.setId(region.getId());
                regionResponseDTO.setName(region.getNameUZ());

                CategoryEntity category = categoryService.get(entity.getCategoryId());
                CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
                categoryResponseDTO.setId(category.getId());
                categoryResponseDTO.setName(category.getNameUZ());
                return toFullInfoDTO(entity, regionResponseDTO, categoryResponseDTO);
            }
            case "ru" -> {
                RegionEntity region = regionService.get(entity.getRegionId());
                RegionResponseDTO regionResponseDTO = new RegionResponseDTO();
                regionResponseDTO.setId(region.getId());
                regionResponseDTO.setName(region.getNameRU());

                CategoryEntity category = categoryService.get(entity.getCategoryId());
                CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
                categoryResponseDTO.setId(category.getId());
                categoryResponseDTO.setName(category.getNameRU());
                return toFullInfoDTO(entity, regionResponseDTO, categoryResponseDTO);
            }
            case "eng" -> {
                RegionEntity region = regionService.get(entity.getRegionId());
                RegionResponseDTO regionResponseDTO = new RegionResponseDTO();
                regionResponseDTO.setId(region.getId());
                regionResponseDTO.setName(region.getNameEN());

                CategoryEntity category = categoryService.get(entity.getCategoryId());
                CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
                categoryResponseDTO.setId(category.getId());
                categoryResponseDTO.setName(category.getNameEN());
                return toFullInfoDTO(entity, regionResponseDTO, categoryResponseDTO);
            }
            default -> {
                throw new MethodNotAllowedException("Lang not found");
            }
        }
    }

    public ArticleFullDTO toFullInfoDTO(ArticleEntity entity,
                                        RegionResponseDTO regionResponseDTO,
                                        CategoryResponseDTO categoryResponseDTO) {

        ArticleFullDTO dto = new ArticleFullDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setRegion(regionResponseDTO);
        dto.setCategory(categoryResponseDTO);
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setSharedCount(entity.getSharedCount());
        dto.setViewCount(entity.getViewCount());
//         dto.setLikeCount(entity.getLikeCount());
        return dto;
    }

    public List<ArticleShortInfoDTO> getLast4ExceptGivenId(String id) {
        List<ArticleShortInfoMapper> listOfArticle = articleRepository.findAll4(id, 4);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        listOfArticle.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }

    public List<ArticleShortInfoDTO> getLast4MostView() {
        List<ArticleShortInfoMapper> listOfArticle = articleRepository.find4(4);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        listOfArticle.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }

    public List<ArticleShortInfoDTO> get5ByTypeAndRegion(Integer typeId, Integer regionId) {
        List<ArticleEntity> listOfArticle = articleRepository.find5ByTypeAndRegion(typeId, regionId);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        listOfArticle.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }

    public List<ArticleShortInfoDTO> get5ByCategory(Integer categoryId) {
        List<ArticleEntity> listOfArticle = articleRepository.get5ByCategoryId(categoryId);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        listOfArticle.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }

    public Page<ArticleShortInfoDTO> getArticleByRegionIdPaging(int page, int size, Integer id) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<ArticleEntity> pageObj = articleRepository.findAllByRegionId(paging, id);

        Long totalCount = pageObj.getTotalElements();

        List<ArticleEntity> entityList = pageObj.getContent();
        List<ArticleShortInfoDTO> list = new LinkedList<>();
        entityList.forEach(entity -> {
            list.add(toArticleShortInfo(entity));
        });
        Page<ArticleShortInfoDTO> response = new PageImpl<ArticleShortInfoDTO>(list, paging, totalCount);
        return response;
    }


    public Page<ArticleShortInfoDTO> getArticleByCategoryIdPaging(int page, int size, Integer id) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<ArticleEntity> pageObj = articleRepository.findAllByCategoryId(paging, id);

        Long totalCount = pageObj.getTotalElements();

        List<ArticleEntity> entityList = pageObj.getContent();
        List<ArticleShortInfoDTO> list = new LinkedList<>();
        entityList.forEach(entity -> {
            list.add(toArticleShortInfo(entity));
        });
        Page<ArticleShortInfoDTO> response = new PageImpl<ArticleShortInfoDTO>(list, paging, totalCount);
        return response;
    }

    public int increaseViewCount(String articleId) {
        Optional<ArticleEntity> articleEntity = articleRepository.findById(articleId);
        if (articleEntity.isEmpty()) {
            throw new ItemNotFoundException("Item not found");
        }
        ArticleEntity articleEntity1 = new ArticleEntity();
        Integer a = articleEntity1.getViewCount();
        if(a==null){
            a=1;
        }
        else {
            a++;
        }
        Integer result = articleRepository.updateViewCount(a, articleId);
        return result;
    }


}