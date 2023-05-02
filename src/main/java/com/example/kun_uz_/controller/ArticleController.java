package com.example.kun_uz_.controller;
import com.example.kun_uz_.dto.JwtDTO.JwtDTO;
import com.example.kun_uz_.dto.article.ArticleListDTO;
import com.example.kun_uz_.dto.article.ArticleRequestDTO;
import com.example.kun_uz_.dto.article.ArticleShortInfoDTO;
import com.example.kun_uz_.enums.ArticleStatus;
import com.example.kun_uz_.enums.ProfileRole;
import com.example.kun_uz_.service.ArticleService;
import com.example.kun_uz_.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("")
    public ResponseEntity<ArticleRequestDTO> create(@RequestBody @Valid ArticleRequestDTO dto,
                                                    @RequestHeader("Authorization") String authorization) {
        JwtDTO jwt = JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.create(dto, jwt.getId()));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ArticleRequestDTO> update(@RequestBody ArticleRequestDTO dto,
                                                    @RequestHeader("Authorization") String authorization,
                                                    @PathVariable("id") String articleId) {
        JwtDTO jwt = JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.update(dto, articleId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id,
                                    @RequestHeader("Authorization") String authorization) {
        JwtDTO jwt = JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleService.delete(id));
    }

    @PostMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") String id,
                                          @RequestParam String status,
                                          @RequestHeader("Authorization") String authorization) {
        JwtDTO jwt = JwtUtil.getJwtDTO(authorization, ProfileRole.PUBLISHER);
        return ResponseEntity.ok(articleService.changeStatus(ArticleStatus.valueOf(status), id, jwt.getId()));
    }

    @GetMapping("/type/{id}/five")
    public ResponseEntity<List<ArticleShortInfoDTO>> get5ByTypeId(@PathVariable("id") Integer id) {
//        return ResponseEntity.ok(articleService.getLast5ByTypeId(id));
        return ResponseEntity.ok(articleService.getLast5ByTypeId2(id));
    }


    @GetMapping("/type/{id}/three")
    public ResponseEntity<List<ArticleShortInfoDTO>> get3ByTypeId(@PathVariable("id") Integer id) {
//        return ResponseEntity.ok(articleService.getLast5ByTypeId(id));
        return ResponseEntity.ok(articleService.getLast3ByTypeId(id));
    }

    @GetMapping("get-last8")
    public ResponseEntity<List<ArticleShortInfoDTO>> get3ByTypeId2(@RequestBody ArticleListDTO listDTO) {
//        return ResponseEntity.ok(articleService.getLast5ByTypeId(id));
        return ResponseEntity.ok(articleService.getLast8WithoutList(listDTO.getList()));
    }

    @GetMapping("/get-id-lang")
    public ResponseEntity<?> getByIdAndLang(@RequestParam("id") String id, @RequestParam("language") String language) {
        return ResponseEntity.ok(articleService.getByIdAndLanguage(id, language));
    }

    @GetMapping("/get-last4")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast4Except(@RequestParam("id") String id){
        return ResponseEntity.ok((articleService.getLast4ExceptGivenId(id)));
    }

    @GetMapping("/get-last4view")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast4MostView(){
        return ResponseEntity.ok((articleService.getLast4MostView()));
    }

    @GetMapping("/get5by-type-region")
    public ResponseEntity<List<ArticleShortInfoDTO>> get5ByTypeAndRegion(@RequestParam("type") Integer typeId, @RequestParam("region") Integer regionId){
        return ResponseEntity.ok((articleService.get5ByTypeAndRegion(typeId, regionId)));
    }

    @GetMapping("/get5by-category")
    public ResponseEntity<List<ArticleShortInfoDTO>> get5ByCategory(@RequestParam("category") Integer categoryId){
        return ResponseEntity.ok(articleService.get5ByCategory(categoryId));
    }

    @GetMapping(value = "/paging")
    public ResponseEntity<Page<ArticleShortInfoDTO>> paging(@RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "size", defaultValue = "30") int size,
                                                            @RequestParam(value = "id") Integer id) {
        return ResponseEntity.ok(articleService.getArticleByRegionIdPaging(page, size, id));
    }

    @GetMapping(value = "/paging-category")
    public ResponseEntity<Page<ArticleShortInfoDTO>> pagingWithCategory(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                        @RequestParam(value = "size", defaultValue = "30") int size,
                                                                        @RequestParam(value = "id") Integer id) {
        return ResponseEntity.ok(articleService.getArticleByCategoryIdPaging(page, size, id));
    }

    @GetMapping("/view-count")
    public ResponseEntity<Integer> increaseViewCount(@RequestParam("id") String articleId){
        return ResponseEntity.ok(articleService.increaseViewCount(articleId));
    }


}
