package com.generation.crm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.crm_backend.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    

}
