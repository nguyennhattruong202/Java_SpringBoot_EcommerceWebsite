package com.ecommerce.controller;

import com.ecommerce.dto.request.AuthenticationRequest;
import com.ecommerce.service.implement.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    private final JwtService jwtService;

    public AuthenticationController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/token")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Info");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authenticated");
    }

    @GetMapping("/token2")
    public ResponseEntity<String> authenticate2() {
        log.info("Info");
        return ResponseEntity.status(HttpStatus.OK).body("Token");
    }
}
