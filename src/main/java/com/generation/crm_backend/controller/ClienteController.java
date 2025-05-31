package com.generation.crm_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.crm_backend.dto.ClienteRequestDTO;
import com.generation.crm_backend.model.Cliente;
import com.generation.crm_backend.model.Cliente.TipoPessoa;
import com.generation.crm_backend.repository.ClienteRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Cliente> getAll() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getById(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(resp -> ResponseEntity.ok(resp))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public List<Cliente> getAllByNome(@PathVariable String nome) {

        return clienteRepository.findAllByNomeContainingIgnoreCase(nome);
    }

    @GetMapping("/email/{email}")
    public Optional<Cliente> getAllByEmail(@PathVariable String email) {

        return clienteRepository.findByEmailContainingIgnoreCase(email);
    }

    @GetMapping("/telefone/{telefone}")
    public Optional<Cliente> getAllByTelefone(@PathVariable String telefone) {

        return clienteRepository.findByTelefoneContainingIgnoreCase(telefone);
    }

    @GetMapping("/tipoPessoa/{tipoPessoa}")
    public List<Cliente> getAllByTipoPessoa(@PathVariable TipoPessoa tipoPessoa) {

        return clienteRepository.findAllByTipoPessoa(tipoPessoa);
    }

    @GetMapping("/cnpj/{cnpj}")
    public Optional<Cliente> getByCnpj(@PathVariable String cnpj) {

        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        return clienteRepository.findByCnpj(cnpjLimpo);
    }

    @GetMapping("/cpf/{cpf}")
    public Optional<Cliente> getByCpf(@PathVariable String cpf) {

        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        return clienteRepository.findByCpf(cpfLimpo);
    }

    @GetMapping("/leadScore/{leadScore}")
    public List<Cliente> getByLeadScore(@PathVariable Float leadScore) {

        return clienteRepository.findByLeadScore(leadScore);
    }

    @GetMapping("/leadScore/max/{leadScore}")
    public List<Cliente> getByLeadScoreGreaterThanEqual(@PathVariable Float leadScore) {

        return clienteRepository.findAllByLeadScoreGreaterThanEqual(leadScore);
    }

    @GetMapping("/leadScore/min/{leadScore}")
    public List<Cliente> getByLeadScoreLessThanEqual(@PathVariable Float leadScore) {

        return clienteRepository.findAllByLeadScoreLessThanEqual(leadScore);
    }

    @PostMapping
    public ResponseEntity<Cliente> post(@RequestBody ClienteRequestDTO clienteResponseDTO) {

        Cliente cliente = new Cliente();

        cliente.setNome(clienteResponseDTO.getNome());
        cliente.setEmail(clienteResponseDTO.getEmail());
        cliente.setFoto(clienteResponseDTO.getFoto());
        cliente.setTelefone(clienteResponseDTO.getTelefone());
        cliente.setTipoPessoa(clienteResponseDTO.getTipoPessoa());
        cliente.setCpf(clienteResponseDTO.getCpf());
        cliente.setCnpj(clienteResponseDTO.getCnpj());
        cliente.setLeadScore(clienteResponseDTO.getLeadScore());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteRepository.save(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> put(@PathVariable Long id, @Valid @RequestBody Cliente clienteDetalhes) {

        Optional<Cliente> clienteExistenteOptional = clienteRepository.findById(id);

        if (!clienteExistenteOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Cliente clienteExistente = clienteExistenteOptional.get();

        clienteExistente.setNome(clienteDetalhes.getNome());
        clienteExistente.setEmail(clienteDetalhes.getEmail());
        clienteExistente.setFoto(clienteDetalhes.getFoto());
        clienteExistente.setTelefone(clienteDetalhes.getTelefone());
        clienteExistente.setTipoPessoa(clienteDetalhes.getTipoPessoa());
        clienteExistente.setCpf(clienteDetalhes.getCpf());
        clienteExistente.setCnpj(clienteDetalhes.getCnpj());
        clienteExistente.setLeadScore(clienteDetalhes.getLeadScore());

        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);

        return ResponseEntity.ok(clienteAtualizado);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clienteRepository.deleteById(id);
    }

}
