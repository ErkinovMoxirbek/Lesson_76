package com.example.repository;

import com.example.entity.ArticleEntity;
import com.example.enums.ArticleStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends CrudRepository<ArticleEntity, Integer> {
    Optional<ArticleEntity> findById(String id);
    @Query(value = "select * from article where type_id = :type_id and status = :status order by created_date desc limit :count",nativeQuery = true)
    List<ArticleEntity> articleShortInfo(@Param("type_id")Integer typeId, @Param("status") ArticleStatus status, @Param("count")Integer count);
}
