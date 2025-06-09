package com.openapi.cloud.core.repository;

import com.openapi.cloud.core.model.entities.Role;
import com.openapi.cloud.core.model.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
