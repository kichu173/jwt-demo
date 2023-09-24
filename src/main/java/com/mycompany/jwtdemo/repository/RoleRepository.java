package com.mycompany.jwtdemo.repository;

import com.mycompany.jwtdemo.entity.RoleEntity;
import com.mycompany.jwtdemo.enums.ERole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(ERole eRole);
}
