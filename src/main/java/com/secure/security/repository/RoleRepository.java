package com.secure.security.repository;

import com.secure.security.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByCode(String code);

    Optional<Role> findByName(String name);

    Boolean existsByCode(String code);

    Boolean existsByName(String name);
}
