package com.example.controller;

import com.example.dto.article.ArticleDTO;
import com.example.dto.article.ArticleInfoDTO;
import com.example.dto.article.ArticleResponseDTO;
import com.example.dto.article.ArticleShortInfoDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.service.ArticleService;
import com.example.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
@AllArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    @PostMapping("/")
    public ResponseEntity<ArticleResponseDTO> create(@RequestBody @Valid ArticleResponseDTO dto,
                                                     @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.create(dto, jwtDTO.getId()));
    }
    @PostMapping("/{id}")
    public ResponseEntity<ArticleDTO> update(@RequestHeader("Authorization") String authorization,
                                             @RequestBody ArticleDTO dto, @PathVariable String id) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.update(dto, id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id,
                                          @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.delete(id));
    }
    @PutMapping("/{id}")
    private ResponseEntity<ArticleDTO> changeStatus(@PathVariable String id) {
        return ResponseEntity.ok(articleService.changeStatus(id));
    }
    @GetMapping("/{typeId}")
    public ResponseEntity<List<ArticleDTO>> articleShortInfo(@PathVariable("typeId") Integer typeId) {
        List<ArticleDTO> dtoList = articleService.articleShortInfo(typeId);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{list}")
    public ResponseEntity<List<ArticleDTO>> articleShortInfo(@PathVariable List<Integer> list) {
        List<ArticleDTO> dtoList = articleService.articleShortInfo(list);
        return ResponseEntity.ok(dtoList);
    }
    @GetMapping("/get_by_id_and_lang")
    public ResponseEntity<ArticleInfoDTO> articleFullInfo(@RequestParam("id") String id,
                                                          @RequestParam("lang") String lang) {
        ArticleInfoDTO dto = articleService.articleFullInfo(id, lang);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/4-article-by-types")
    public ResponseEntity<List<ArticleShortInfoDTO>> get4ArticleByTypes(@RequestParam("typeId") Integer typeId,
                                                                        @RequestParam("id") String id) {
        List<ArticleShortInfoDTO> list = articleService.get4ArticleByTypes(typeId, id);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/4most_read")
    public ResponseEntity<List<ArticleShortInfoDTO>> articleShortInfo() {
        List<ArticleShortInfoDTO> dtoList = articleService.articleShortInfo();
        return ResponseEntity.ok(dtoList);
    }
    @GetMapping("/region-article")
    public ResponseEntity<List<ArticleShortInfoDTO>> getRegionArticle(@RequestParam("id") Integer id, @RequestParam("size") int size,
                                                                      @RequestParam("page") int page) {
        List<ArticleShortInfoDTO> list = articleService.getRegionArticle(id, size, page);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/5-category-article/{id}")
    public ResponseEntity<List<ArticleShortInfoDTO>> get5CategoryArticle(@PathVariable Integer id) {
        List<ArticleShortInfoDTO> list = articleService.get5CategoryArticle(id);
        return ResponseEntity.ok(list);
    }
}