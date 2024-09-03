package com.example.mbaningapijpapractice.features.auth;

import com.example.mbaningapijpapractice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
