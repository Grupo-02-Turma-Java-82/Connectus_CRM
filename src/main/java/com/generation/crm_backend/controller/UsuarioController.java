package com.generation.crm_backend.controller;

import java.util.List;
import java.util.Optional; // Importe Optional, caso não esteja sendo usado implicitamente

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.crm_backend.model.Usuario;
import com.generation.crm_backend.repository.UsuarioRepository;
import com.generation.crm_backend.service.UsuarioService;

// Importações do Swagger/OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
// Em produção, ajuste as origens para domínios específicos por segurança.
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Usuário", description = "Gerencia os Usuários da aplicação CRM") // Define o grupo para esta API no Swagger UI
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Lista todos os Usuários", description = "Retorna uma lista de todos os usuários cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum usuário encontrado (lista vazia)")
    })
    @GetMapping("/all")
    public ResponseEntity <List<Usuario>> getAll(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Boa prática para listas vazias
        }
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Busca Usuário por ID", description = "Retorna os detalhes de um usuário específico com base no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
            .map(resposta -> ResponseEntity.ok(resposta))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cadastra um novo Usuário", description = "Registra um novo usuário no sistema. Valida os dados antes de persistir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados do usuário inválidos ou usuário já existente")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> postUsuario(@RequestBody @Valid Usuario usuario) {
        return usuarioService.cadastrarUsuario(usuario)
            .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
            .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @Operation(summary = "Atualiza um Usuário existente", description = "Modifica os dados de um usuário já cadastrado, utilizando o ID como referência na URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para atualização"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos")
    })
    @PutMapping("/autualizar/{id}") // Atenção ao "autualizar" - talvez seja "atualizar"?
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        // Assume que o `atualizarUsuario` do serviço já valida o ID e o corpo da requisição.
        return usuarioService.atualizarUsuario(id, usuario)
            .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}