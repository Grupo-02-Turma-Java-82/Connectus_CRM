package com.generation.crm_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.generation.crm_backend.model.Oportunidade;
import com.generation.crm_backend.model.StatusOportunidade;

@Repository
public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {

	List<Oportunidade> findByStatus(StatusOportunidade status);

	List<Oportunidade> findByClienteId(Long idCliente); //alteracao de findByIdCliente

	List<Oportunidade> findByUsuarioId(Long idUsuario); //alteracao de findByIdUsuario

	List<Oportunidade> findAllByTituloContainingIgnoreCase(String titulo);

}