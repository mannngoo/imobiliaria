package com.example.imobiliaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.imobiliaria.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByEmail(String email);
}