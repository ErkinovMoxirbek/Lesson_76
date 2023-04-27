package com.example.controller;

import com.example.dto.AuthDTO;
import com.example.dto.AuthResponseDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.RegistrationDTO;
import com.example.exp.AppBadRequestException;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO dto) {
        try{
            return ResponseEntity.ok(authService.login(dto));
        }catch (AppBadRequestException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PostMapping(value = "/registration")
    public ResponseEntity<?> registration (@RequestBody RegistrationDTO dto){
        try{
            return ResponseEntity.ok(authService.registration(dto));
        }catch (AppBadRequestException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
