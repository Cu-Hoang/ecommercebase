package com.project.ecommercebase.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecommercebase.data.entity.Code;

public interface CodeRepository extends JpaRepository<Code, UUID> {}
