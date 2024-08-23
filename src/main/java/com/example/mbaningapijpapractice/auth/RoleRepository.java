package com.example.mbaningapijpapractice.auth;

import com.example.mbaningapijpapractice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
