package com.generation.crm_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.generation.crm_backend.dto.ClienteRequestDTO;
import com.generation.crm_backend.model.Cliente;
import com.generation.crm_backend.model.Cliente.TipoPessoa;
import com.generation.crm_backend.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Clientes", description = "API para gerenciamento de Clientes no CRM")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista de todos os clientes cadastrados. Considere adicionar paginação para grandes volumes de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes recuperada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
    })
    @GetMapping
    public ResponseEntity<List<Cliente>> getAll() {

        return ResponseEntity.ok(clienteService.getAll());
    }

    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente específico baseado no seu ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o ID fornecido (resposta global já configurada)")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getById(
            @Parameter(description = "ID do cliente a ser buscado", required = true, example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(clienteService.getById(id));
    }

    @Operation(summary = "Buscar clientes por nome", description = "Retorna uma lista de clientes cujo nome contenha o termo pesquisado (case-insensitive).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca por nome realizada com sucesso (pode retornar lista vazia)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Cliente>> getAllByNome(
            @Parameter(description = "Termo para buscar no nome dos clientes", required = true, example = "Silva") @PathVariable String nome) {
        return ResponseEntity.ok(clienteService.getAllByNome(nome));
    }

    @Operation(summary = "Buscar cliente por e-mail", description = "Retorna um cliente específico baseado no seu e-mail (case-insensitive). O e-mail é único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o e-mail fornecido")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> getByEmail(
            @Parameter(description = "E-mail do cliente a ser buscado", required = true, example = "joao.silva@example.com") @PathVariable String email) {
        return ResponseEntity.ok(clienteService.getByEmail(email));
    }

    @Operation(summary = "Buscar cliente por telefone", description = "Retorna um cliente específico baseado no seu telefone (case-insensitive, após normalização).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o telefone fornecido")
    })
    @GetMapping("/telefone/{telefone}")
    public ResponseEntity<Cliente> getByTelefone(
            @Parameter(description = "Telefone do cliente a ser buscado", required = true, example = "(11)987654321") @PathVariable String telefone) {
        return ResponseEntity.ok(clienteService.getByTelefone(telefone));
    }

    @Operation(summary = "Buscar clientes por tipo de pessoa", description = "Retorna uma lista de clientes filtrados por tipo (FISICA ou JURIDICA).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca por tipo de pessoa realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/tipoPessoa/{tipoPessoa}")
    public ResponseEntity<List<Cliente>> getAllByTipoPessoa(
            @Parameter(description = "Tipo de pessoa para filtrar os clientes", required = true, example = "FISICA", schema = @Schema(implementation = TipoPessoa.class)) @PathVariable TipoPessoa tipoPessoa) {
        return ResponseEntity.ok(clienteService.getAllByTipoPessoa(tipoPessoa));
    }

    @Operation(summary = "Buscar cliente por CNPJ", description = "Retorna um cliente específico baseado no seu CNPJ (após normalização para apenas números). O CNPJ é único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o CNPJ fornecido")
    })
    @GetMapping("/cnpj")
    public ResponseEntity<Cliente> getByCnpj(
            @Parameter(description = "CNPJ do cliente a ser buscado (com ou sem máscara)", required = true, example = "12.345.678/0001-99") @RequestParam String cnpj) {
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        return ResponseEntity.ok(clienteService.getByCnpj(cnpjLimpo));
    }

    @Operation(summary = "Buscar cliente por CPF", description = "Retorna um cliente específico baseado no seu CPF (após normalização para apenas números). O CPF é único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o CPF fornecido")
    })
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Cliente> getByCpf(
            @Parameter(description = "CPF do cliente a ser buscado (com ou sem máscara)", required = true, example = "123.456.789-00") @PathVariable String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        return ResponseEntity.ok(clienteService.getByCpf(cpfLimpo));
    }

    @Operation(summary = "Buscar clientes por Lead Score exato", description = "Retorna uma lista de clientes com um Lead Score específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca por Lead Score realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/leadScore/{leadScore}")
    public ResponseEntity<List<Cliente>> getAllByLeadScore(
            @Parameter(description = "Valor do Lead Score para buscar", required = true, example = "7.5") @PathVariable Float leadScore) {
        return ResponseEntity.ok(clienteService.getAllByLeadScore(leadScore));
    }

    @Operation(summary = "Buscar clientes por Lead Score maior ou igual", description = "Retorna uma lista de clientes com Lead Score maior ou igual ao valor fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/leadScore/maior/{leadScore}")
    public ResponseEntity<List<Cliente>> getAllByLeadScoreGreaterThanEqual(
            @Parameter(description = "Valor mínimo do Lead Score (inclusivo)", required = true, example = "5.0") @PathVariable Float leadScore) {
        return ResponseEntity.ok(clienteService.getAllByLeadScoreGreaterThanEqual(leadScore));
    }

    @Operation(summary = "Buscar clientes por Lead Score menor ou igual", description = "Retorna uma lista de clientes com Lead Score menor ou igual ao valor fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/leadScore/menor/{leadScore}")
    public ResponseEntity<List<Cliente>> getAllByLeadScoreLessThanEqual(
            @Parameter(description = "Valor máximo do Lead Score (inclusivo)", required = true, example = "8.0") @PathVariable Float leadScore) {
        return ResponseEntity.ok(clienteService.getAllByLeadScoreLessThanEqual(leadScore));
    }

    @Operation(summary = "Criar um novo cliente", description = "Cadastra um novo cliente no sistema. Retorna o cliente criado com status 201 e o URI do novo recurso no cabeçalho Location.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos na requisição")
    })
    @PostMapping
    public ResponseEntity<Cliente> post(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do cliente para criação", required = true, content = @Content(schema = @Schema(implementation = ClienteRequestDTO.class))) @Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        Cliente clienteCriado = clienteService.create(clienteRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clienteCriado.getId())
                .toUri();
        return ResponseEntity.created(location).body(clienteCriado);
    }

    @Operation(summary = "Atualizar um cliente existente", description = "Atualiza os dados de um cliente existente identificado pelo seu ID. Retorna o cliente atualizado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos na requisição"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o ID fornecido")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> put(
            @Parameter(description = "ID do cliente a ser atualizado", required = true, example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do cliente para atualização", required = true, content = @Content(schema = @Schema(implementation = ClienteRequestDTO.class))) @Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        return ResponseEntity.ok(clienteService.update(id, clienteRequestDTO));
    }

    @Operation(summary = "Excluir um cliente", description = "Remove um cliente do sistema baseado no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso (sem conteúdo de retorno)"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o ID fornecido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do cliente a ser excluído", required = true, example = "1") @PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}