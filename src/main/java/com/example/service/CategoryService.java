package com.example.service;

import com.example.dto.CategoryDTO;
import com.example.dto.CategoryLangDTO;
import com.example.entity.CategoryEntity;
import com.example.exps.AppBadRequestException;
import com.example.exps.ItemNotFoundException;
import com.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public Integer create(CategoryDTO dto, Integer adminId) {

        CategoryEntity entity = new CategoryEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setId(adminId);
        categoryRepository.save(entity); // save profile

        dto.setId(entity.getId());
        return entity.getId();
    }
    public Boolean update(Integer id, CategoryDTO categoryDto) {
        CategoryEntity entity = get(id);
        if (entity == null) {
            throw new ItemNotFoundException("Region not found");
        }
        entity.setNameUz(categoryDto.getNameUz());
        entity.setNameRu(categoryDto.getNameRu());
        entity.setNameEn(categoryDto.getNameEn());

        categoryRepository.save(entity);
        return true;
    }
    public CategoryEntity get(Integer id) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Category not found: " + id);
        }
        return optional.get();
    }

    public Boolean deleteById(Integer id) {
        CategoryEntity entity = get(id);
        if (entity == null) {
            throw new ItemNotFoundException("Profile not found.");
        }
        entity.setVisible(false);
        entity.setId(4);
        categoryRepository.save(entity);
        return true;
    }

    public Page<CategoryDTO> getAll(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable paging = PageRequest.of(page - 1, size, sort);
        Page<CategoryEntity> pageObj = categoryRepository.findAll(paging);

        Long totalCount = pageObj.getTotalElements();

        List<CategoryEntity> entityList = pageObj.getContent();
        List<CategoryDTO> dtoList = new LinkedList<>();

        if (!pageObj.equals(null)) {
            for (CategoryEntity entity : entityList) {
                CategoryDTO dto = new CategoryDTO();
                dto.setId(entity.getId());
                dto.setNameUz(entity.getNameUz());
                dto.setNameRu(entity.getNameRu());
                dto.setNameEn(entity.getNameEn());
                dto.setCreatedDate(entity.getCreatedDate());
                dto.setVisible(entity.getVisible());
                dtoList.add(dto);
            }
            Page<CategoryDTO> response = new PageImpl<CategoryDTO>(dtoList, paging, totalCount);
            return response;
        }
        throw new ItemNotFoundException("ArticleType is empty");
    }

    public List<CategoryLangDTO> getLang(String lang) {
        List<CategoryLangDTO> list = new LinkedList<>();

        switch (lang) {
            case "uz" -> list.addAll(getUzLang());
            case "ru" -> list.addAll(getRuLang());
            case "eng" -> list.addAll(getEngLang());
            case "null" -> throw new ItemNotFoundException("Item not found");
            default -> throw new AppBadRequestException("Error");
        }
        return list;
    }

    private List<CategoryLangDTO> getEngLang() {
        List<CategoryLangDTO> list = new LinkedList<>();
        Iterable<CategoryEntity> entity = categoryRepository.findAll();
        for (CategoryEntity categoryEntity : entity) {
            CategoryLangDTO dto = new CategoryLangDTO();
            dto.setId(categoryEntity.getId());
            dto.setName(categoryEntity.getNameEn());
            list.add(dto);
        }
        return list;
    }

    private List<CategoryLangDTO> getRuLang() {
        List<CategoryLangDTO> list = new LinkedList<>();
        Iterable<CategoryEntity> entity = categoryRepository.findAll();
        for (CategoryEntity categoryEntity : entity) {
            CategoryLangDTO dto = new CategoryLangDTO();
            dto.setId(categoryEntity.getId());
            dto.setName(categoryEntity.getNameRu());
            list.add(dto);
        }
        return list;
    }
    private List<CategoryLangDTO> getUzLang() {
        List<CategoryLangDTO> list = new LinkedList<>();
        Iterable<CategoryEntity> entity = categoryRepository.findAll();
        for (CategoryEntity categoryEntity : entity) {
            CategoryLangDTO dto = new CategoryLangDTO();
            dto.setId(categoryEntity.getId());
            dto.setName(categoryEntity.getNameUz());
            list.add(dto);
        }
        return list;
    }
}
