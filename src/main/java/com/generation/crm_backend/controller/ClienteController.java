package com.generation.crm_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import com.generation.crm_backend.repository.ClienteRepository;
import com.generation.crm_backend.dto.ClienteRequestDTO;
import com.generation.crm_backend.model.Cliente;
import com.generation.crm_backend.model.Cliente.TipoPessoa;
import com.generation.crm_backend.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Clientes", description = "API para gestão de Clientes no CRM")
public class ClienteController {

  @Autowired
  private ClienteService clienteService;

  @Operation(summary = "Listar todos os clientes com paginação", description = "Retorna uma lista paginada de todos os clientes registados. Permite especificar a página, tamanho da página e critérios de ordenação.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista paginada de clientes recuperada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
  })
  @GetMapping
  public ResponseEntity<Page<Cliente>> getAll(
      @Parameter(description = "Número da página (começa em 0)", example = "0", name = "pagina", in = ParameterIn.QUERY) @RequestParam(value = "pagina", defaultValue = "0") int numeroPagina,

      @Parameter(description = "Tamanho da página", example = "10", name = "tamanho", in = ParameterIn.QUERY) @RequestParam(value = "tamanho", defaultValue = "10") int tamanhoPagina,

      @Parameter(description = "Campo para ordenação (ex: 'nome', 'id', 'email')", example = "id", name = "ordenarPor", in = ParameterIn.QUERY) @RequestParam(value = "ordenarPor", defaultValue = "id") String campoOrdenacao,

      @Parameter(description = "Direção da ordenação ('asc' para ascendente, 'desc' para descendente)", example = "asc", name = "direcao", in = ParameterIn.QUERY, schema = @Schema(allowableValues = {
          "asc",
          "desc" })) @RequestParam(value = "direcao", defaultValue = "asc") String direcaoOrdenacao) {

    Page<Cliente> paginaDeClientes = clienteService.getAll(numeroPagina, tamanhoPagina, campoOrdenacao,
        direcaoOrdenacao);
    return ResponseEntity.ok(paginaDeClientes);
  }

  @Operation(summary = "Procurar cliente por ID", description = "Retorna um cliente específico baseado no seu ID único.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
      @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o ID fornecido")
  })
  @GetMapping("/{id}")
  public ResponseEntity<Cliente> getById(@Parameter(description = "ID do cliente a ser procurado", required = true, example = "1") @PathVariable Long id) {
	Optional<Cliente> cliente = clienteService.getById(id);

	if (cliente.isPresent()) {		
		return ResponseEntity.ok(cliente.get());
	} else {
		return ResponseEntity.notFound().build();
	}
  }

  @Operation(summary = "Procurar clientes por nome com paginação e ordenação", description = "Retorna uma página de clientes cujo nome contenha o termo pesquisado (case-insensitive), com opções de paginação e ordenação.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Procura por nome realizada com sucesso (pode retornar página vazia)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
  })
  @GetMapping("/nome/{nome}")
  public ResponseEntity<Page<Cliente>> getAllByNome(
      @Parameter(description = "Termo para procurar no nome dos clientes", required = true, example = "Silva") @PathVariable String nome,
      @Parameter(description = "Número da página (começa em 0)", example = "0", name = "pagina", in = ParameterIn.QUERY) @RequestParam(value = "pagina", defaultValue = "0") int numeroPagina,
      @Parameter(description = "Tamanho da página", example = "10", name = "tamanho", in = ParameterIn.QUERY) @RequestParam(value = "tamanho", defaultValue = "10") int tamanhoPagina,
      @Parameter(description = "Campo para ordenação (ex: 'nome', 'id')", example = "nome", name = "ordenarPor", in = ParameterIn.QUERY) @RequestParam(value = "ordenarPor", defaultValue = "nome") String campoOrdenacao,
      @Parameter(description = "Direção da ordenação ('asc' para ascendente, 'desc' para descendente)", example = "asc", name = "direcao", in = ParameterIn.QUERY, schema = @Schema(allowableValues = {
          "asc",
          "desc" })) @RequestParam(value = "direcao", defaultValue = "asc") String direcaoOrdenacao) {

    Page<Cliente> clientesPaginados = clienteService.getAllByNome(nome, numeroPagina, tamanhoPagina,
        campoOrdenacao, direcaoOrdenacao);
    return ResponseEntity.ok(clientesPaginados);
  }

  @Operation(summary = "Procurar cliente por e-mail", description = "Retorna um cliente específico baseado no seu e-mail (case-insensitive). O e-mail é único.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
      @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o e-mail fornecido")
  })
  @GetMapping("/email/{email}")
  public ResponseEntity<Cliente> getByEmail(
      @Parameter(description = "E-mail do cliente a ser procurado", required = true, example = "joao.silva@example.com") @PathVariable String email) {
    return ResponseEntity.ok(clienteService.getByEmail(email));
  }

  @Operation(summary = "Procurar cliente por telefone", description = "Retorna um cliente específico baseado no seu telefone (case-insensitive, após normalização).")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
      @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o telefone fornecido")
  })
  @GetMapping("/telefone/{telefone}")
  public ResponseEntity<Cliente> getByTelefone(
      @Parameter(description = "Telefone do cliente a ser procurado", required = true, example = "(11)987654321") @PathVariable String telefone) {
    return ResponseEntity.ok(clienteService.getByTelefone(telefone));
  }

  @Operation(summary = "Procurar clientes por tipo de pessoa com paginação", description = "Retorna uma página de clientes filtrados por tipo (FISICA ou JURIDICA).")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Procura por tipo de pessoa realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
  })
  @GetMapping("/tipoPessoa/{tipoPessoa}")
  public ResponseEntity<Page<Cliente>> getAllByTipoPessoa(
      @Parameter(description = "Tipo de pessoa para filtrar os clientes", required = true, example = "FISICA", schema = @Schema(implementation = TipoPessoa.class)) @PathVariable TipoPessoa tipoPessoa,
      @Parameter(description = "Número da página (começa em 0)", example = "0", name = "pagina", in = ParameterIn.QUERY) @RequestParam(value = "pagina", defaultValue = "0") int numeroPagina,
      @Parameter(description = "Tamanho da página", example = "10", name = "tamanho", in = ParameterIn.QUERY) @RequestParam(value = "tamanho", defaultValue = "10") int tamanhoPagina,
      @Parameter(description = "Campo para ordenação", example = "nome", name = "ordenarPor", in = ParameterIn.QUERY) @RequestParam(value = "ordenarPor", defaultValue = "nome") String campoOrdenacao,
      @Parameter(description = "Direção da ordenação ('asc', 'desc')", example = "asc", name = "direcao", in = ParameterIn.QUERY, schema = @Schema(allowableValues = {
          "asc",
          "desc" })) @RequestParam(value = "direcao", defaultValue = "asc") String direcaoOrdenacao) {
    Page<Cliente> clientesPaginados = clienteService.getAllByTipoPessoa(tipoPessoa, numeroPagina,
        tamanhoPagina, campoOrdenacao, direcaoOrdenacao);
    return ResponseEntity.ok(clientesPaginados);
  }

  @Operation(summary = "Procurar cliente por CNPJ", description = "Retorna um cliente específico baseado no seu CNPJ (após normalização para apenas números). O CNPJ é único.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
      @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o CNPJ fornecido")
  })
  @GetMapping("/cnpj")
  public ResponseEntity<Cliente> getByCnpj(
      @Parameter(description = "CNPJ do cliente a ser procurado (com ou sem máscara)", required = true, example = "12.345.678/0001-99", name = "cnpj", in = ParameterIn.QUERY) @RequestParam String cnpj) {
    String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
    return ResponseEntity.ok(clienteService.getByCnpj(cnpjLimpo));
  }

  @Operation(summary = "Procurar cliente por CPF", description = "Retorna um cliente específico baseado no seu CPF (após normalização para apenas números). O CPF é único.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
      @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o CPF fornecido")
  })
  @GetMapping("/cpf/{cpf}")
  public ResponseEntity<Cliente> getByCpf(
      @Parameter(description = "CPF do cliente a ser procurado (com ou sem máscara)", required = true, example = "123.456.789-00") @PathVariable String cpf) {
    String cpfLimpo = cpf.replaceAll("[^0-9]", "");
    return ResponseEntity.ok(clienteService.getByCpf(cpfLimpo));
  }

  @Operation(summary = "Procurar clientes por Lead Score exato com paginação", description = "Retorna uma página de clientes com um Lead Score específico.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Procura por Lead Score realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
  })
  @GetMapping("/leadScore/{leadScore}")
  public ResponseEntity<Page<Cliente>> getAllByLeadScore(
      @Parameter(description = "Valor do Lead Score para procurar", required = true, example = "7.5") @PathVariable Float leadScore,
      @Parameter(description = "Número da página (começa em 0)", example = "0", name = "pagina", in = ParameterIn.QUERY) @RequestParam(value = "pagina", defaultValue = "0") int numeroPagina,
      @Parameter(description = "Tamanho da página", example = "10", name = "tamanho", in = ParameterIn.QUERY) @RequestParam(value = "tamanho", defaultValue = "10") int tamanhoPagina,
      @Parameter(description = "Campo para ordenação", example = "id", name = "ordenarPor", in = ParameterIn.QUERY) @RequestParam(value = "ordenarPor", defaultValue = "id") String campoOrdenacao,
      @Parameter(description = "Direção da ordenação ('asc', 'desc')", example = "asc", name = "direcao", in = ParameterIn.QUERY, schema = @Schema(allowableValues = {
          "asc",
          "desc" })) @RequestParam(value = "direcao", defaultValue = "asc") String direcaoOrdenacao) {
    Page<Cliente> clientesPaginados = clienteService.getAllByLeadScore(leadScore, numeroPagina,
        tamanhoPagina, campoOrdenacao, direcaoOrdenacao);
    return ResponseEntity.ok(clientesPaginados);
  }

  @Operation(summary = "Procurar clientes por Lead Score maior ou igual com paginação", description = "Retorna uma página de clientes com Lead Score maior ou igual ao valor fornecido.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Procura realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
  })
  @GetMapping("/leadScore/maior/{leadScore}")
  public ResponseEntity<Page<Cliente>> getAllByLeadScoreGreaterThanEqual(
      @Parameter(description = "Valor mínimo do Lead Score (inclusivo)", required = true, example = "5.0") @PathVariable Float leadScore,
      @Parameter(description = "Número da página (começa em 0)", example = "0", name = "pagina", in = ParameterIn.QUERY) @RequestParam(value = "pagina", defaultValue = "0") int numeroPagina,
      @Parameter(description = "Tamanho da página", example = "10", name = "tamanho", in = ParameterIn.QUERY) @RequestParam(value = "tamanho", defaultValue = "10") int tamanhoPagina,
      @Parameter(description = "Campo para ordenação", example = "leadScore", name = "ordenarPor", in = ParameterIn.QUERY) @RequestParam(value = "ordenarPor", defaultValue = "leadScore") String campoOrdenacao,
      @Parameter(description = "Direção da ordenação ('asc', 'desc')", example = "asc", name = "direcao", in = ParameterIn.QUERY, schema = @Schema(allowableValues = {
          "asc",
          "desc" })) @RequestParam(value = "direcao", defaultValue = "asc") String direcaoOrdenacao) {
    Page<Cliente> clientesPaginados = clienteService.getAllByLeadScoreGreaterThanEqual(leadScore,
        numeroPagina, tamanhoPagina, campoOrdenacao, direcaoOrdenacao);
    return ResponseEntity.ok(clientesPaginados);
  }

  @Operation(summary = "Procurar clientes por Lead Score menor ou igual com paginação", description = "Retorna uma página de clientes com Lead Score menor ou igual ao valor fornecido.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Procura realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
  })
  @GetMapping("/leadScore/menor/{leadScore}")
  public ResponseEntity<Page<Cliente>> getAllByLeadScoreLessThanEqual(
      @Parameter(description = "Valor máximo do Lead Score (inclusivo)", required = true, example = "8.0") @PathVariable Float leadScore,
      @Parameter(description = "Número da página (começa em 0)", example = "0", name = "pagina", in = ParameterIn.QUERY) @RequestParam(value = "pagina", defaultValue = "0") int numeroPagina,
      @Parameter(description = "Tamanho da página", example = "10", name = "tamanho", in = ParameterIn.QUERY) @RequestParam(value = "tamanho", defaultValue = "10") int tamanhoPagina,
      @Parameter(description = "Campo para ordenação", example = "leadScore", name = "ordenarPor", in = ParameterIn.QUERY) @RequestParam(value = "ordenarPor", defaultValue = "leadScore") String campoOrdenacao,
      @Parameter(description = "Direção da ordenação ('asc', 'desc')", example = "asc", name = "direcao", in = ParameterIn.QUERY, schema = @Schema(allowableValues = {
          "asc",
          "desc" })) @RequestParam(value = "direcao", defaultValue = "asc") String direcaoOrdenacao) {
    Page<Cliente> clientesPaginados = clienteService.getAllByLeadScoreLessThanEqual(leadScore, numeroPagina,
        tamanhoPagina, campoOrdenacao, direcaoOrdenacao);
    return ResponseEntity.ok(clientesPaginados);
  }

  @Operation(summary = "Criar um novo cliente", description = "Regista um novo cliente no sistema. Retorna o cliente criado com status 201 e o URI do novo recurso no cabeçalho Location.")
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