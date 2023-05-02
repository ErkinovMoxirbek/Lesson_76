package com.example.service;

import com.example.dto.article.ArticleTypeDTO;
import com.example.dto.article.ArticleTypeRequestDTO;
import com.example.entity.ArticleTypeEntity;
import com.example.exps.AppBadRequestException;
import com.example.exps.ArticleTypeNotFoundException;
import com.example.repository.ArticleTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleTypeService {
    private final ArticleTypeRepository articleTypeRepository;

    public ArticleTypeDTO updateById(Integer id, ArticleTypeDTO dto) {
        Optional<ArticleTypeEntity> optional = articleTypeRepository.findById(id);
        if (optional.isPresent()) {
            ArticleTypeEntity entity = optional.get();
            if (dto.getNameUz() != null) {
                entity.setNameUz(dto.getNameUz());
            }
            if (dto.getNameEn() != null) {
                entity.setNameEn(dto.getNameEn());
            }
            if (dto.getNameRu() != null) {
                entity.setNameRu(dto.getNameRu());
            }
            articleTypeRepository.save(entity);
            return dto;
        }
        throw new AppBadRequestException("not found article type");
    }

    public ArticleTypeRequestDTO create(ArticleTypeRequestDTO dto, Integer adminId) {
        isValidProfile(dto);
        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setPrtId(adminId);
        articleTypeRepository.save(entity);
        return dto;
    }

    public void isValidProfile(ArticleTypeRequestDTO dto) {
        // throw ...
        if (dto.getNameUz() == null) {
            throw new AppBadRequestException("invalid name uz");
        }
        if (dto.getNameEn() == null) {
            throw new AppBadRequestException("invalid name en");
        }
        if (dto.getNameRu() == null) {
            throw new AppBadRequestException("invalid name ru");
        }
    }

    public Boolean deleteById(Integer id) {
        Optional<ArticleTypeEntity> optional = articleTypeRepository.findById(id);
        if (optional.isPresent()) {
            ArticleTypeEntity entity = optional.get();
            entity.setVisible(false);
            articleTypeRepository.save(entity);
            return true;
        }
        throw new ArticleTypeNotFoundException("article type not found");
    }

    public List<ArticleTypeDTO> getAll() {
        Iterable<ArticleTypeEntity> entityList = articleTypeRepository.findAll();
        List<ArticleTypeDTO> dtoList = new LinkedList<>();
        for (ArticleTypeEntity entity : entityList) {
            dtoList.add(toDTO(entity));
        }
        return dtoList;
    }

    public ArticleTypeDTO toDTO(ArticleTypeEntity entity) {
        ArticleTypeDTO dto = new ArticleTypeDTO();
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setNameUz(entity.getNameUz());
        return dto;
    }

    public List<ArticleTypeDTO> getByLang(String lang) {
        List<ArticleTypeEntity> entityList = switch (lang) {
            case "en" -> articleTypeRepository.findByNameEng();
            case "ru" -> articleTypeRepository.findByNameRu();
            case "uz" -> articleTypeRepository.findByNameUz();
            default -> throw new AppBadRequestException("not found '" + lang + "'");
        };
        List<ArticleTypeDTO> dtoList = new LinkedList<>();
        for (ArticleTypeEntity entity : entityList) {
            dtoList.add(toDTO(entity));
        }
        return dtoList;
    }

    public ArticleTypeEntity get(Integer typeId) {
        Optional<ArticleTypeEntity> optional = articleTypeRepository.findById(typeId);
        if (optional.isEmpty()) {
            throw new ArticleTypeNotFoundException("not found type");
        }
        return optional.get();
    }

    public ArticleTypeEntity get(String name) {
        ArticleTypeEntity entity = articleTypeRepository.findByName(name);
        if (entity == null) {
            throw new ArticleTypeNotFoundException("not fount type");
        }
        return entity;
    }
}