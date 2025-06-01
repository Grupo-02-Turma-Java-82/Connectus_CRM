package com.generation.crm_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.crm_backend.model.Cliente;
import com.generation.crm_backend.model.Cliente.TipoPessoa;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

  List<Cliente> findAllByNomeContainingIgnoreCase(String nome);

  Optional<Cliente> findByEmailIgnoreCase(String email);

  Optional<Cliente> findByTelefoneIgnoreCase(String telefone);

  List<Cliente> findAllByTipoPessoa(TipoPessoa tipoPessoa);

  Optional<Cliente> findByCnpj(String cnpj);

  Optional<Cliente> findByCpf(String cpf);

  List<Cliente> findByLeadScore(Float leadScore);

  // Extra

  // Pegar todos clientes com o leadScore maior ou igual ao valor informado
  List<Cliente> findAllByLeadScoreGreaterThanEqual(Float leadScore);

  // Pegar todos clientes com o leadScore menor ou igual ao valor informado
  List<Cliente> findAllByLeadScoreLessThanEqual(Float leadScore);

}