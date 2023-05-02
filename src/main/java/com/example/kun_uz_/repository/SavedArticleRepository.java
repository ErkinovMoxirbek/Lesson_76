package com.example.kun_uz_.repository;

import com.example.kun_uz_.entity.SavedArticleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedArticleRepository extends CrudRepository<SavedArticleEntity, Integer> {
}
