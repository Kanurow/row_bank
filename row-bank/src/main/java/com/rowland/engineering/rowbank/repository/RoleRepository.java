package com.rowland.engineering.rowbank.repository;

import com.rowland.engineering.rowbank.model.Role;
import com.rowland.engineering.rowbank.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);
}
