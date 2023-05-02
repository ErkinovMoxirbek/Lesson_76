package com.example.kun_uz_.service;

import com.example.kun_uz_.dto.article.SavedArticleDto;
import com.example.kun_uz_.dto.article.SavedArticleResponseDto;
import com.example.kun_uz_.entity.ArticleEntity;
import com.example.kun_uz_.entity.ProfileEntity;
import com.example.kun_uz_.entity.SavedArticleEntity;
import com.example.kun_uz_.repository.SavedArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SavedArticleService {
    @Autowired
    private SavedArticleRepository savedArticleRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ArticleService articleService;

    public Integer create(SavedArticleDto dto) {
        ProfileEntity profileEntity = profileService.get(dto.getProfileId());
        ArticleEntity article = articleService.get(dto.getArticleId());

        SavedArticleEntity articleSavedEntity = new SavedArticleEntity();
        articleSavedEntity.setArticle(article);
        articleSavedEntity.setProfile(profileEntity);
        articleSavedEntity.setCreatedDate(LocalDateTime.now());
        savedArticleRepository.save(articleSavedEntity);
        return dto.getId();
    }

    public Boolean delete(Integer id) {
        savedArticleRepository.deleteById(id);
        return true;
    }

    public List<SavedArticleResponseDto> getList() {
        return null;
    }
}
