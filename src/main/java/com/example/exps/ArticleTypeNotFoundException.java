package com.example.exps;

public class ArticleTypeNotFoundException extends RuntimeException{
    public ArticleTypeNotFoundException(String message) {
        super(message);
    }
}
