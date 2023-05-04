package com.example.kun_uz_.repository;

import com.example.kun_uz_.entity.TagEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<TagEntity,Integer> {
    List<TagEntity> findAll ();
    TagEntity findByName(String name);
}
