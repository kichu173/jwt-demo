package com.mycompany.jwtdemo.service;

import com.mycompany.jwtdemo.dto.ErrorDTO;
import com.mycompany.jwtdemo.dto.JwtResponseDTO;
import com.mycompany.jwtdemo.dto.LoginDTO;
import com.mycompany.jwtdemo.dto.RegisterDTO;
import com.mycompany.jwtdemo.entity.RoleEntity;
import com.mycompany.jwtdemo.entity.UserEntity;
import com.mycompany.jwtdemo.enums.ERole;
import com.mycompany.jwtdemo.exception.BusinessException;
import com.mycompany.jwtdemo.repository.RoleRepository;
import com.mycompany.jwtdemo.repository.UserRepository;
import com.mycompany.jwtdemo.util.JwtUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
        AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public JwtResponseDTO login(LoginDTO loginDTO) {
        Authentication authenticate = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String jwtToken = jwtUtil.generateJWTToken(authenticate);
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(role -> role.getAuthority()).collect(Collectors.toList());
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setFirstName(userDetails.getFirstName());
        jwtResponseDTO.setLastName(userDetails.getLastName());
        jwtResponseDTO.setToken(jwtToken);
        jwtResponseDTO.setRoles(roles);
        jwtResponseDTO.setId(userDetails.getId());

        return jwtResponseDTO;
    }

    @Override
    public Long signUp(RegisterDTO registerDTO) {
        List<ErrorDTO> errorDTOS = new ArrayList<>();
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            errorDTOS.add(new ErrorDTO("AUTH_001", "Email is already taken"));
            throw new BusinessException(errorDTOS);
        }
        String hashedPassword = passwordEncoder.encode(registerDTO.getPassword());
        Set<RoleEntity> roleEntities = new HashSet<>();
        Optional<RoleEntity> optRole = null;
        if (registerDTO.getRole() != null && registerDTO.getRole().equals("ADMIN")) {
            optRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN);
        } else if (registerDTO.getRole() != null && registerDTO.getRole().equals("MODERATOR")) {
            optRole = roleRepository.findByRoleName(ERole.ROLE_MODERATOR);
        } else {
            optRole = roleRepository.findByRoleName(ERole.ROLE_USER);
        }

        roleEntities.add(optRole.get());
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(registerDTO.getEmail());
        userEntity.setFirstName(registerDTO.getFirstName());
        userEntity.setLastName(registerDTO.getLastName());
        userEntity.setPhone(registerDTO.getPhone());
        userEntity.setPassword(hashedPassword);
        userEntity.setRoles(roleEntities);

        userEntity = userRepository.save(userEntity);
        return userEntity.getId();
    }

}
