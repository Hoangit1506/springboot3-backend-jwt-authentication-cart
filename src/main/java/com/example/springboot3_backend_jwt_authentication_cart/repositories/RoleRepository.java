package com.example.springboot3_backend_jwt_authentication_cart.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springboot3_backend_jwt_authentication_cart.models.ERole;
import com.example.springboot3_backend_jwt_authentication_cart.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}