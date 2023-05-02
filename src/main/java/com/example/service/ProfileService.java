 package com.example.service;

import com.example.dto.jwt.ProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.GeneralStatus;
import com.example.exps.AppBadRequestException;
import com.example.exps.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

 @Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public ProfileDTO create(ProfileDTO dto, Integer adminId) {
        // check - homework
        isValidProfile(dto);

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        entity.setPassword(MD5Util.getMd5Hash(dto.getPassword())); // MD5 ?
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setId(adminId);
        entity.setStatus(GeneralStatus.ACTIVED);
        profileRepository.save(entity); // save profile

        dto.setPassword(null);
        dto.setId(entity.getId());
        return dto;
    }
    public void isValidProfile(ProfileDTO dto) {
        // throw ...
    }
    public Page<ProfileDTO> getAll(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable paging = PageRequest.of(page - 1, size, sort);
        Page<ProfileEntity> pageObj = profileRepository.findAll(paging);

        Long totalCount = pageObj.getTotalElements();

        List<ProfileEntity> entityList = pageObj.getContent();
        List<ProfileDTO> dtoList = new LinkedList<>();
        if (pageObj != null) {
            for (ProfileEntity entity : entityList) {
                ProfileDTO dto = new ProfileDTO();
                dto.setId(entity.getId());
                dto.setName(entity.getName());
                dto.setSurname(entity.getSurname());
                dto.setPhone(entity.getPhone());
                dto.setEmail(entity.getEmail());
                dto.setRole(entity.getRole());
                dtoList.add(dto);
            }
            Page<ProfileDTO> response = new PageImpl<ProfileDTO>(dtoList, paging, totalCount);
            return response;
        }
        throw new ItemNotFoundException("List id empty");
    }
    public ProfileDTO getById(Integer id) {
        ProfileEntity entity = get(id);
        if (entity == null) {
            throw new ItemNotFoundException("Such id not" + id);
        }
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());

        return dto;
    }
    public ProfileEntity get(Integer id) {
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Profile not found: " + id);
        }
        return optional.get();
    }
    private List<ProfileDTO> toDto(List<ProfileEntity> list) {
        List<ProfileDTO> dtoList = new LinkedList<>();
        for (ProfileEntity entity : list) {
            ProfileDTO dto = new ProfileDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setSurname(entity.getSurname());
            dto.setPhone(entity.getPhone());
            dto.setEmail(entity.getEmail());
            dto.setRole(entity.getRole());

            dtoList.add(dto);
        }
        return dtoList;
    }
    public Boolean deleteById(Integer id) {
        ProfileEntity entity = get(id);
        if (entity == null) {
            throw new ItemNotFoundException("Profile not found.");
        }
        entity.setVisible(false);
        entity.setId(4);
        profileRepository.save(entity);
        return true;
    }
    public Boolean updateAdmin(Integer id, ProfileDTO profileDto) {
        ProfileEntity entity = get(id);
        if (entity == null) {
            throw new ItemNotFoundException("Profile not found");
        }
        entity.setName(profileDto.getName());
        entity.setSurname(profileDto.getSurname());
        entity.setPhone(profileDto.getPhone());
        entity.setPassword(profileDto.getPassword());
        entity.setEmail(profileDto.getEmail());
        entity.setRole(profileDto.getRole());
        entity.setStatus(profileDto.getStatus());

        profileRepository.save(entity);
        return true;
    }
    public Boolean update(Integer id, ProfileDTO profileDto) {
        ProfileEntity entity = get(id);
        if (entity == null) {
            throw new ItemNotFoundException("Profile not found");
        }

        if (Optional.ofNullable(profileDto.getName()).isPresent()){
            entity.setName(profileDto.getName());
        }
        if (!profileDto.getSurname().isBlank() && profileDto.getSurname() != null){
            entity.setSurname(profileDto.getSurname());
        }if (!profileDto.getName().isBlank() && profileDto.getName() != null){
            entity.setName(profileDto.getName());
        }
        if (!profileDto.getEmail().isBlank() && profileDto.getEmail() != null){
            entity.setEmail(profileDto.getEmail());
        }if (!profileDto.getPhone().isBlank() && profileDto.getPhone() != null){
            entity.setPhone(profileDto.getPhone());
        }
        if (!profileDto.getPassword().isBlank() && profileDto.getPassword() != null){
            entity.setPassword(MD5Util.getMd5Hash(profileDto.getPassword()));
        }
        profileRepository.save(entity);
        return true;
    }
}
