package com.example.exps;

public class ProfileNotFoundException extends RuntimeException{
    public ProfileNotFoundException(String message) {
        super(message);
    }
}
