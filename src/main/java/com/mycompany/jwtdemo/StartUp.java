package com.mycompany.jwtdemo;

import com.mycompany.jwtdemo.entity.RoleEntity;
import com.mycompany.jwtdemo.enums.ERole;
import com.mycompany.jwtdemo.repository.RoleRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartUp implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        Optional<RoleEntity> optRoleAdmin = roleRepository.findByRoleName(ERole.ROLE_ADMIN);
        if (optRoleAdmin.isEmpty()) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setRoleName(ERole.ROLE_ADMIN);
            roleRepository.save(roleEntity);
        }

        Optional<RoleEntity> optRoleModerator = roleRepository.findByRoleName(ERole.ROLE_MODERATOR);
        if (optRoleModerator.isEmpty()) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setRoleName(ERole.ROLE_MODERATOR);
            roleRepository.save(roleEntity);
        }

        Optional<RoleEntity> optRoleUSER = roleRepository.findByRoleName(ERole.ROLE_USER);
        if (optRoleUSER.isEmpty()) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setRoleName(ERole.ROLE_USER);
            roleRepository.save(roleEntity);
        }
    }

}
