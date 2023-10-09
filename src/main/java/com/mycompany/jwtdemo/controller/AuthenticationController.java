package com.mycompany.jwtdemo.controller;

import com.mycompany.jwtdemo.dto.JwtResponseDTO;
import com.mycompany.jwtdemo.dto.LoginDTO;
import com.mycompany.jwtdemo.dto.RegisterDTO;
import com.mycompany.jwtdemo.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/openapi/v1")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        JwtResponseDTO jwtResponseDTO = authenticationService.login(loginDTO);
        return ResponseEntity.ok(jwtResponseDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterDTO registerDTO) {
        Long userId = authenticationService.signUp(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully with id: " + userId);
    }
}
