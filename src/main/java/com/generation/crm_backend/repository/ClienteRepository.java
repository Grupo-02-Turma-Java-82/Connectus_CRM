package com.generation.crm_backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.crm_backend.model.Cliente;
import com.generation.crm_backend.model.Cliente.TipoPessoa;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {

  Page<Cliente> findAllByNomeContainingIgnoreCase(String nome, Pageable pageable);

  Optional<Cliente> findByEmailIgnoreCase(String email);

  Optional<Cliente> findByTelefoneIgnoreCase(String telefone);

  Page<Cliente> findAllByTipoPessoa(TipoPessoa tipoPessoa, Pageable pageable);

  Optional<Cliente> findByCnpj(String cnpj);

  Optional<Cliente> findByCpf(String cpf);

  Page<Cliente> findAllByLeadScore(Float leadScore, Pageable pageable);

  // Extra

  // Pegar todos clientes com o leadScore maior ou igual ao valor informado
  Page<Cliente> findAllByLeadScoreGreaterThanEqual(Float leadScore, Pageable pageable);

  // Pegar todos clientes com o leadScore menor ou igual ao valor informado
  Page<Cliente> findAllByLeadScoreLessThanEqual(Float leadScore, Pageable pageable);

}