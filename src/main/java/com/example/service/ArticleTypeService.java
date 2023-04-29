package com.example.service;

import com.example.dto.ArticleTypeDTO;
import com.example.dto.ArticleTypeLangDTO;
import com.example.entity.ArticleTypeEntity;
import com.example.exps.AppBadRequestException;
import com.example.exps.ItemNotFoundException;
import com.example.repository.ArticleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleTypeService {
    @Autowired
    private ArticleTypeRepository articleTypeRepository;
    public Integer create(ArticleTypeDTO dto, Integer adminId) {

        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setId(adminId);
        articleTypeRepository.save(entity); // save profile

        dto.setId(entity.getId());
        return entity.getId();
    }
    public Boolean update(Integer id, ArticleTypeDTO articleTypeDto) {
        ArticleTypeEntity entity = get(id);
        if (entity == null) {
            throw new ItemNotFoundException("Article not found");
        }
        entity.setNameUz(articleTypeDto.getNameUz());
        entity.setNameRu(articleTypeDto.getNameRu());
        entity.setNameEn(articleTypeDto.getNameEn());

        articleTypeRepository.save(entity);
        return true;
    }
    public ArticleTypeEntity get(Integer id) {
        Optional<ArticleTypeEntity> optional = articleTypeRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Article not found: " + id);
        }
        return optional.get();
    }

    public Boolean deleteById(Integer id) {
        ArticleTypeEntity entity = get(id);
        if (entity == null) {
            throw new ItemNotFoundException("Profile not found.");
        }
        entity.setVisible(false);
        entity.setId(4);
        articleTypeRepository.save(entity);
        return true;
    }

    public Page<ArticleTypeDTO> getAll(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable paging = PageRequest.of(page - 1, size, sort);
        Page<ArticleTypeEntity> pageObj = articleTypeRepository.findAll(paging);

        Long totalCount = pageObj.getTotalElements();

        List<ArticleTypeEntity> entityList = pageObj.getContent();
        List<ArticleTypeDTO> dtoList = new LinkedList<>();

        if (!pageObj.equals(null)) {
            for (ArticleTypeEntity entity : entityList) {
                ArticleTypeDTO dto = new ArticleTypeDTO();
                dto.setId(entity.getId());
                dto.setNameUz(entity.getNameUz());
                dto.setNameRu(entity.getNameRu());
                dto.setNameEn(entity.getNameEn());
                dto.setCreatedDate(entity.getCreatedDate());
                dto.setVisible(entity.getVisible());
                dtoList.add(dto);
            }
            Page<ArticleTypeDTO> response = new PageImpl<ArticleTypeDTO>(dtoList, paging, totalCount);
            return response;
        }
        throw new ItemNotFoundException("ArticleType is empty");
    }

    public List<ArticleTypeLangDTO> getLang(String lang) {
        List<ArticleTypeLangDTO> list = new LinkedList<>();

        switch (lang) {
            case "uz" -> list.addAll(getUzLang());
            case "ru" -> list.addAll(getRuLang());
            case "eng" -> list.addAll(getEngLang());
            case "null" -> throw new ItemNotFoundException("Item not found");
            default -> throw new AppBadRequestException("Error");
        }
        return list;
    }

    private List<ArticleTypeLangDTO> getEngLang() {
        List<ArticleTypeLangDTO> list = new LinkedList<>();
        Iterable<ArticleTypeEntity> entity = articleTypeRepository.findAll();
        for (ArticleTypeEntity articleTypeEntity : entity) {
            ArticleTypeLangDTO dto = new ArticleTypeLangDTO();
            dto.setId(articleTypeEntity.getId());
            dto.setName(articleTypeEntity.getNameEn());
            list.add(dto);
        }
        return list;
    }

    private List<ArticleTypeLangDTO> getRuLang() {
        List<ArticleTypeLangDTO> list = new LinkedList<>();
        Iterable<ArticleTypeEntity> entity = articleTypeRepository.findAll();
        for (ArticleTypeEntity articleTypeEntity : entity) {
            ArticleTypeLangDTO dto = new ArticleTypeLangDTO();
            dto.setId(articleTypeEntity.getId());
            dto.setName(articleTypeEntity.getNameRu());
            list.add(dto);
        }
        return list;
    }
    private List<ArticleTypeLangDTO> getUzLang() {
        List<ArticleTypeLangDTO> list = new LinkedList<>();
        Iterable<ArticleTypeEntity> entity = articleTypeRepository.findAll();
        for (ArticleTypeEntity articleTypeEntity : entity) {
            ArticleTypeLangDTO dto = new ArticleTypeLangDTO();
            dto.setId(articleTypeEntity.getId());
            dto.setName(articleTypeEntity.getNameUz());
            list.add(dto);
        }
        return list;
    }
}
