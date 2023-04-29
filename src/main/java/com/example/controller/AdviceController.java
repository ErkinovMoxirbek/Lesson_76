package com.example.controller;

import com.example.exp.AppBadRequestException;
import com.example.exp.ArticleNotFoundException;
import com.example.exp.ItemNotFoundException;
import com.example.exp.MethodNotAllowedException;
import com.example.exps.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class AdviceController {
    @ExceptionHandler({AppBadRequestException.class, ArticleTypeNotFoundException.class,
            CategoryNotFoundException.class, ItemNotFoundException.class,
            MethodNotAllowedException.class, RegionNotFoundException.class, ArticleNotFoundException.class,
            ProfileNotFoundException.class, AttachNotFoundException.class})
    public ResponseEntity<String> handleException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
