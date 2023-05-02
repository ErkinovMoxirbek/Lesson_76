package com.example.controller;

import com.example.dto.auth.AuthDTO;
import com.example.dto.registration.RegistrationDTO;
import com.example.dto.registration.RegistrationResponseDTO;
import com.example.exps.AppBadRequestException;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    @GetMapping("/email/verification/{jwt}")
    public ResponseEntity<RegistrationResponseDTO> emailVerification(@PathVariable("jwt") String jwt) {
        return ResponseEntity.ok(authService.emailVerification(jwt));
    }
}
