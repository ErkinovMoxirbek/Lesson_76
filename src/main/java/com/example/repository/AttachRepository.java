package com.example.repository;

import com.example.entity.AttachEntity;
import com.example.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AttachRepository extends CrudRepository<AttachEntity,Integer> {
    Optional<AttachEntity> findById(String id);
    Page<AttachEntity> findAll (Pageable pageable);
}
