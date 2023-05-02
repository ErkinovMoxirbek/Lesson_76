package com.example.repository;

import com.example.entity.ArticleEntity;
import com.example.entity.ArticleTypeEntity;
import com.example.entity.CategoryEntity;
import com.example.entity.RegionEntity;
import com.example.enums.ArticleStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends CrudRepository<ArticleEntity, Integer> {
    Optional<ArticleEntity> findById(String id);
    @Query(value = "select a from ArticleEntity a where type=:typeEntity order by createdDate limit 3", nativeQuery = true)
    List<ArticleEntity> findAllByType(@Param("typeEntity") ArticleTypeEntity typeEntity);
    @Query(value = "select a from ArticleEntity a where id not in :idList order by createdDate desc limit 8 ", nativeQuery = true)
    List<ArticleEntity> articleShortInfo(@Param("idList") List<Integer> idList);
    @Query(value = "select a from ArticleEntity a order by a.viewCount limit 4", nativeQuery = true)
    List<ArticleEntity> mostReadArticles();
    @Query("select a from ArticleEntity a where a.type=:type and a.region=:region and a.visible=true order by a.createdDate limit 5")
    List<ArticleEntity> findByTypeAndRegion(ArticleTypeEntity type, RegionEntity region);
    Page<ArticleEntity> findByRegionId(Integer region_id, Pageable pageable);
    @Query("select a from ArticleEntity a where a.category=:category")
    List<ArticleEntity> get5CategoryArticle(@Param("category") CategoryEntity category);
    Page<ArticleEntity> findByCategoryId(Integer id, Pageable pageable);
    @Query("select a from ArticleEntity a where a.type=:type and a.id <>:id and a.visible=true order by a.createdDate desc limit 4")
    List<ArticleEntity> findByTypeIdAndIdNot(@Param("type") ArticleTypeEntity type, @Param("id") String id);
    @Query("SELECT new ArticleEntity(id,title,description,attachId,publishedDate) From ArticleEntity where status =:status and visible = true and typeId =:typeId order by createdDate desc limit 5")
    List<ArticleEntity> find5ByTypeId(@Param("typeId") Integer typeId, @Param("status") ArticleStatus status);
}