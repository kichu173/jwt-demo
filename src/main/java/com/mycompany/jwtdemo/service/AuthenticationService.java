package com.mycompany.jwtdemo.service;

import com.mycompany.jwtdemo.dto.JwtResponseDTO;
import com.mycompany.jwtdemo.dto.LoginDTO;
import com.mycompany.jwtdemo.dto.RegisterDTO;

public interface AuthenticationService {
    JwtResponseDTO login(LoginDTO loginDTO);
    Long signUp(RegisterDTO registerDTO);
}
