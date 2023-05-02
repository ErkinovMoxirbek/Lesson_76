package com.example.service;

import com.example.dto.region.RegionDTO;
import com.example.dto.region.RegionLangDTO;
import com.example.entity.RegionEntity;
import com.example.exps.AppBadRequestException;
import com.example.exps.ItemNotFoundException;
import com.example.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;
    public Integer create(RegionDTO dto, Integer adminId) {

        RegionEntity entity = new RegionEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setId(adminId);
        regionRepository.save(entity); // save profile

        dto.setId(entity.getId());
        return entity.getId();
    }
    public Boolean update(Integer id, RegionDTO dto) {
        RegionEntity entity = get(id);
        if (entity == null) {
            throw new ItemNotFoundException("Region not found");
        }
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());

        regionRepository.save(entity);
        return true;
    }
    public RegionEntity get(Integer id) {
        Optional<RegionEntity> optional = regionRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Article not found: " + id);
        }
        return optional.get();
    }

    public Boolean deleteById(Integer id) {
        RegionEntity entity = get(id);
        if (entity == null) {
            throw new ItemNotFoundException("Profile not found.");
        }
        entity.setVisible(false);
        entity.setId(4);
        regionRepository.save(entity);
        return true;
    }

    public Page<RegionDTO> getAll(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable paging = PageRequest.of(page - 1, size, sort);
        Page<RegionEntity> pageObj = regionRepository.findAll(paging);

        Long totalCount = pageObj.getTotalElements();

        List<RegionEntity> entityList = pageObj.getContent();
        List<RegionDTO> dtoList = new LinkedList<>();

        if (!pageObj.equals(null)) {
            for (RegionEntity entity : entityList) {
                RegionDTO dto = new RegionDTO();
                dto.setId(entity.getId());
                dto.setNameUz(entity.getNameUz());
                dto.setNameRu(entity.getNameRu());
                dto.setNameEn(entity.getNameEn());
                dto.setCreatedDate(entity.getCreatedDate());
                dto.setVisible(entity.getVisible());
                dtoList.add(dto);
            }
            Page<RegionDTO> response = new PageImpl<RegionDTO>(dtoList, paging, totalCount);
            return response;
        }
        throw new ItemNotFoundException("ArticleType is empty");
    }

    public List<RegionLangDTO> getLang(String lang) {
        List<RegionLangDTO> list = new LinkedList<>();

        switch (lang) {
            case "uz" -> list.addAll(getUzLang());
            case "ru" -> list.addAll(getRuLang());
            case "eng" -> list.addAll(getEngLang());
            case "null" -> throw new ItemNotFoundException("Item not found");
            default -> throw new AppBadRequestException("Error");
        }
        return list;
    }

    private List<RegionLangDTO> getEngLang() {
        List<RegionLangDTO> list = new LinkedList<>();
        Iterable<RegionEntity> entity = regionRepository.findAll();
        for (RegionEntity regionEntity : entity) {
            RegionLangDTO dto = new RegionLangDTO();
            dto.setId(regionEntity.getId());
            dto.setName(regionEntity.getNameEn());
            list.add(dto);
        }
        return list;
    }

    private List<RegionLangDTO> getRuLang() {
        List<RegionLangDTO> list = new LinkedList<>();
        Iterable<RegionEntity> entity = regionRepository.findAll();
        for (RegionEntity regionEntity : entity) {
            RegionLangDTO dto = new RegionLangDTO();
            dto.setId(regionEntity.getId());
            dto.setName(regionEntity.getNameRu());
            list.add(dto);
        }
        return list;
    }
    private List<RegionLangDTO> getUzLang() {
        List<RegionLangDTO> list = new LinkedList<>();
        Iterable<RegionEntity> entity = regionRepository.findAll();
        for (RegionEntity regionEntity : entity) {
            RegionLangDTO dto = new RegionLangDTO();
            dto.setId(regionEntity.getId());
            dto.setName(regionEntity.getNameUz());
            list.add(dto);
        }
        return list;
    }
}
