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
import com.generation.crm_backend.model.StatusOportunidade;
import com.generation.crm_backend.repository.OportunidadeRepository;
import com.generation.crm_backend.service.OportunidadeService;

// Importações do Swagger/OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/oportunidades")
@CrossOrigin(origins = "*", allowedHeaders = "*")

@Tag(name = "Oportunidade", description = "Gerencia as Oportunidades de Negócio no CRM") // Define o grupo para esta API no Swagger UI
public class OportunidadeController {

	@Autowired
	private OportunidadeService oportunidadeService;
	
	@Autowired
	private OportunidadeRepository oportunidadeRepository;

	@Operation(summary = "Lista todas as Oportunidades", description = "Retorna uma lista de todas as oportunidades de negócio cadastradas no sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de oportunidades retornada com sucesso"),
			@ApiResponse(responseCode = "204", description = "Nenhuma oportunidade encontrada (lista vazia)")
	})
	@GetMapping
	public ResponseEntity<List<Oportunidade>> getAll() {

		return ResponseEntity.ok(oportunidadeService.findAll());
	}

	@Operation(summary = "Busca Oportunidade por ID", description = "Retorna os detalhes de uma oportunidade específica com base no seu ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Oportunidade encontrada com sucesso"),
			@ApiResponse(responseCode = "404", description = "Oportunidade não encontrada")
	})
	@GetMapping("/{id}")
	public ResponseEntity<Oportunidade> getById(@PathVariable Long id) {
		return oportunidadeService.findById(id).map(resposta -> ResponseEntity.ok(resposta))

				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@Operation(summary = "Busca Oportunidades por Título", description = "Retorna uma lista de oportunidades cujo título contenha a string informada, ignorando maiúsculas e minúsculas.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de oportunidades retornada com sucesso"),
			@ApiResponse(responseCode = "204", description = "Nenhuma oportunidade encontrada com o título informado")
	})
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

	@Operation(summary = "Cria uma nova Oportunidade", description = "Adiciona uma nova oportunidade de negócio ao sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Oportunidade criada com sucesso",
					content = @Content(schema = @Schema(implementation = Oportunidade.class))), // Documenta o corpo da resposta
			@ApiResponse(responseCode = "400", description = "Dados da oportunidade inválidos")
	})
	@PostMapping
	public ResponseEntity<Oportunidade> post(@Valid @RequestBody Oportunidade oportunidade) {
		// validaçao na camada de servico!!
		return ResponseEntity.status(HttpStatus.CREATED).body(oportunidadeService.save(oportunidade));
	}

	@Operation(summary = "Atualiza uma Oportunidade existente", description = "Modifica os dados de uma oportunidade já cadastrada. É necessário fornecer o ID da oportunidade no corpo da requisição.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Oportunidade atualizada com sucesso",
					content = @Content(schema = @Schema(implementation = Oportunidade.class))),
			@ApiResponse(responseCode = "400", description = "ID da oportunidade ausente ou inválido"),
			@ApiResponse(responseCode = "404", description = "Oportunidade não encontrada para atualização")
	})
	@PutMapping
	public ResponseEntity<Oportunidade> put(@Valid @RequestBody Oportunidade oportunidade) {

		if (oportunidade.getId() == null) {
			return ResponseEntity.badRequest().build(); // ID é obrigatório para PUT
		}

		if (oportunidadeRepository.existsById(oportunidade.getId())) {
			return ResponseEntity.status(HttpStatus.OK).body(oportunidadeRepository.save(oportunidade));
		}

		return ResponseEntity.status(HttpStatus.OK).body(oportunidadeService.update(oportunidade));
	}

	@PutMapping("/{id}/status/{novoStatus}")
	public ResponseEntity<Oportunidade> atualizarStatusOportunidade(@PathVariable Long id,
			@PathVariable StatusOportunidade novoStatus) {
		return ResponseEntity.ok(oportunidadeService.atualizarStatusOportunidade(id, novoStatus));
	}

	@Operation(summary = "Exclui uma Oportunidade", description = "Remove uma oportunidade de negócio do sistema com base no seu ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Oportunidade excluída com sucesso"),
			@ApiResponse(responseCode = "404", description = "Oportunidade não encontrada para exclusão")
	})
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // Garante o status 204 mesmo sem corpo de resposta
	public void delete(@PathVariable Long id) {
		Optional<Oportunidade> oportunidade = oportunidadeRepository.findById(id);

		if(oportunidade.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Oportunidade não encontrada!");
		}

		oportunidadeService.deleteById(id);
	}
}