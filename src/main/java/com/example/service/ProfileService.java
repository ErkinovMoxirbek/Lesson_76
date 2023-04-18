 package com.example.service;

import com.example.dto.ProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.exp.AppBadRequestException;
import com.example.repository.ProfileRepository;
import com.example.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    public ProfileDTO create(ProfileDTO dto) {

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
        profileRepository.save(entity); // save profile

        dto.setPassword(null);
        dto.setId(entity.getId());
        return dto;
    }

    public void isValidProfile(ProfileDTO dto) {
        if(dto.getName() == null || dto.getName().isBlank()){
            throw new AppBadRequestException("Name >>NULL<<");
        }
        if(dto.getSurname() == null || dto.getSurname().isBlank()){
            throw new AppBadRequestException("Surname >>NULL<<");
        }
        if(dto.getPhone() == null || dto.getPhone().isBlank()){
            throw new AppBadRequestException("Phone >>NULL<<");
        }
        if(dto.getEmail() == null || dto.getEmail().isBlank()){
            throw new AppBadRequestException("Email >>NULL<<");
        }
        if(dto.getPassword() == null || dto.getPassword().isBlank()){
            throw new AppBadRequestException("Password >>NULL<<");
        }
        if(dto.getRole() == null ){
            throw new AppBadRequestException("Role >>NULL<<");
        }
    }
}
