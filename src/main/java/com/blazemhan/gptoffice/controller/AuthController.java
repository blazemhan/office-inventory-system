package com.blazemhan.gptoffice.controller;

import com.blazemhan.gptoffice.entity.User;
import com.blazemhan.gptoffice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User request){

        authService.register(request);
        return ResponseEntity.ok("User registered successfully");

    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User request){
        authService.authenticate(request);
        return ResponseEntity.ok("User logged in successfully");
    }
}

