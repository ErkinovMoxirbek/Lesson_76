package com.example.controller;

import com.example.dto.ArticleDTO;
import com.example.dto.ArticleRequestDTO;
import com.example.dto.JwtDTO;
import com.example.enums.ArticleStatus;
import com.example.enums.ProfileRole;
import com.example.exps.MethodNotAllowedException;
import com.example.service.ArticleService;
import com.example.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                                                    @PathVariable("id") Integer id) {
        JwtDTO jwt = JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.update(dto, jwt.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                    @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.delete(id));
    }

    @PostMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Integer id,
                                          @RequestParam String status,
                                          @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.PUBLISHER);
        return ResponseEntity.ok(articleService.changeStatus(ArticleStatus.valueOf(status), id));
    }

    /*Get Last 5 Article By Types  ordered_by_created_date
            (Berilgan types bo'yicha oxirgi 5ta pubished bo'lgan article ni return qiladi.)
            ArticleShortInfo*/
    @GetMapping("/get-last-5")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast5(@RequestParam("type_id") Integer id) {
        return ResponseEntity.ok(articleService.getLastByCount(id, 5));
    }
    @GetMapping("/get-last-3")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast3(@RequestParam("type_id") Integer id) {
        return ResponseEntity.ok(articleService.getLastByCount(id, 3));
    }

    /*    7. Get Last 8  Articles witch id not included in given list.
     */

    @PostMapping("/get-last-given-list")
    public ResponseEntity<List<ArticleShortInfoDTO>>getLast8(@RequestBody List<Integer> countList){
        return ResponseEntity.ok(articleService.getLastGivenList(countList));
    }
}
