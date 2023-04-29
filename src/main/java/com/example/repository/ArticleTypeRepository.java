package com.example.repository;

import com.example.entity.ArticleTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleTypeRepository extends CrudRepository<ArticleTypeEntity, Integer>,
        PagingAndSortingRepository<ArticleTypeEntity, Integer> {
    @Query("select id,nameEn from ArticleTypeEntity where visible=true ")
    List<ArticleTypeEntity> findByNameEng();

    @Query("select id,nameRu from ArticleTypeEntity where visible=true ")
    List<ArticleTypeEntity> findByNameRu();

    @Query("select id,nameUz from ArticleTypeEntity where visible=true ")
    List<ArticleTypeEntity> findByNameUz();

    @Query("select a from ArticleTypeEntity as a where (a.nameUz=:name or a.nameRu=:name or a.nameEn=:name) and a.visible=true ")
    ArticleTypeEntity findByName(@Param("name") String name);
}
