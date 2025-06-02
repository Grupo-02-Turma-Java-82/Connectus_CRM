package com.generation.crm_backend.controller;

import java.util.List;

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

import com.generation.crm_backend.model.Oportunidade;
import com.generation.crm_backend.model.StatusOportunidade;
import com.generation.crm_backend.service.OportunidadeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/oportunidades")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OportunidadeController {

	@Autowired
	private OportunidadeService oportunidadeService;

	@GetMapping
	public ResponseEntity<List<Oportunidade>> getAll() {

		return ResponseEntity.ok(oportunidadeService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Oportunidade> getById(@PathVariable Long id) {
		return oportunidadeService.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<Object> getByTitulo(@PathVariable String titulo) {
		List<Oportunidade> oportunidades = oportunidadeService.findAllByTituloContainingIgnoreCase(titulo);
		if (oportunidades.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(oportunidades);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<Oportunidade>> getByStatus(@PathVariable StatusOportunidade status) {
		List<Oportunidade> oportunidades = oportunidadeService.findByStatus(status);
		if (oportunidades.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(oportunidades);
	}

	@GetMapping("/cliente/{idCliente}")
	public ResponseEntity<List<Oportunidade>> getByClienteId(@PathVariable Long idCliente) {
		List<Oportunidade> oportunidades = oportunidadeService.findByClienteId(idCliente);
		if (oportunidades.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(oportunidades);
	}

	@GetMapping("/usuario/{idUsuario}")
	public ResponseEntity<List<Oportunidade>> getByUsuarioId(@PathVariable Long idUsuario) {
		List<Oportunidade> oportunidades = oportunidadeService.findByUsuarioId(idUsuario);
		if (oportunidades.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(oportunidades);
	}

	@PostMapping
	public ResponseEntity<Oportunidade> post(@Valid @RequestBody Oportunidade oportunidade) {
		// validaçao na camada de servico!!
		return ResponseEntity.status(HttpStatus.CREATED).body(oportunidadeService.save(oportunidade));
	}

	@PutMapping
	public ResponseEntity<Oportunidade> put(@Valid @RequestBody Oportunidade oportunidade) {

		return ResponseEntity.status(HttpStatus.OK).body(oportunidadeService.update(oportunidade));
	}

	@PutMapping("/{id}/status/{novoStatus}")
	public ResponseEntity<Oportunidade> atualizarStatusOportunidade(@PathVariable Long id,
			@PathVariable StatusOportunidade novoStatus) {
		return ResponseEntity.ok(oportunidadeService.atualizarStatusOportunidade(id, novoStatus));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {

		oportunidadeService.deleteById(id);
	}
}