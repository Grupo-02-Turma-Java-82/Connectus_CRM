package com.generation.crm_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.crm_backend.model.Oportunidade;
import com.generation.crm_backend.repository.OportunidadeRepository;
import com.generation.crm_backend.service.OportunidadeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/oportunidades")
@CrossOrigin(origins = "", allowedHeaders = "")
public class OportunidadeController {

	@Autowired
	private OportunidadeService oportunidadeService;

	@Autowired
	private OportunidadeRepository oportunidadeRepository;

	@GetMapping
	public ResponseEntity<List<Oportunidade>> getAll() {

		return ResponseEntity.ok(oportunidadeService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Oportunidade> getById(@PathVariable Long id) {

		return oportunidadeService.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// @GetMapping("/titulo/{titulo}")
	// public ResponseEntity<Object> getByTitulo(@PathVariable String titulo) {

	// return
	// ResponseEntity.ok(oportunidadeService.findAllByTituloContainingIgnoreCase(titulo));
	// }

	@PostMapping
	public ResponseEntity<Oportunidade> post(@Valid @RequestBody Oportunidade oportunidade) {

		return ResponseEntity.status(HttpStatus.CREATED).body(oportunidadeService.save(oportunidade));
	}

	@PutMapping
	public ResponseEntity<Oportunidade> put(@Valid @RequestBody Oportunidade oportunidade) {
		if (oportunidade.getId() == null) {
			return ResponseEntity.badRequest().build();
		}

		if (oportunidadeRepository.existsById(oportunidade.getId())) {

			return ResponseEntity.status(HttpStatus.OK).body(oportunidadeService.save(oportunidade));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {

		Optional<Oportunidade> oportunidade = oportunidadeRepository.findById(id);

		if (oportunidade.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Oportunidade não encontrada!");
		}

		oportunidadeRepository.deleteById(id);
	}
}