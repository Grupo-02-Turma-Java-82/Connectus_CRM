package com.generation.crm_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.generation.crm_backend.dto.ClienteRequestDTO;
import com.generation.crm_backend.model.Cliente;
import com.generation.crm_backend.model.Cliente.TipoPessoa;
import com.generation.crm_backend.repository.ClienteRepository;

@Service
@Transactional
public class ClienteService {

  @Autowired
  private ClienteRepository clienteRepository;

  @Transactional(readOnly = true)
  public List<Cliente> getAll() {
    return clienteRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Cliente getById(Long id) {
    return clienteRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado", null));
  }

  @Transactional(readOnly = true)
  public List<Cliente> getAllByNome(String nome) {
    return clienteRepository.findAllByNomeContainingIgnoreCase(nome);
  }

  @Transactional(readOnly = true)
  public Cliente getByEmail(String email) {
    return clienteRepository.findByEmailIgnoreCase(email)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado com o email: " + email));
  }

  @Transactional(readOnly = true)
  public Cliente getByTelefone(String telefone) {
    return clienteRepository.findByTelefoneIgnoreCase(telefone)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Cliente não encontrado com o telefone: " + telefone));
  }

  @Transactional(readOnly = true)
  public List<Cliente> getAllByTipoPessoa(TipoPessoa tipoPessoa) {
    return clienteRepository.findAllByTipoPessoa(tipoPessoa);
  }

  @Transactional(readOnly = true)
  public Cliente getByCpf(String cpf) {
    return clienteRepository.findByCpf(cpf)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Cliente não encontrado com o CPF: " + cpf));
  }

  @Transactional(readOnly = true)
  public Cliente getByCnpj(String cnpj) {
    return clienteRepository.findByCnpj(cnpj)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Cliente não encontrado com o CNPJ: " + cnpj));
  }

  @Transactional(readOnly = true)
  public List<Cliente> getAllByLeadScore(Float leadScore) {
    return clienteRepository.findByLeadScore(leadScore);
  }

  @Transactional(readOnly = true)
  public List<Cliente> getAllByLeadScoreGreaterThanEqual(Float leadScore) {
    return clienteRepository.findAllByLeadScoreGreaterThanEqual(leadScore);
  }

  @Transactional(readOnly = true)
  public List<Cliente> getAllByLeadScoreLessThanEqual(Float leadScore) {
    return clienteRepository.findAllByLeadScoreLessThanEqual(leadScore);
  }

  private record DocumentosValidados(String cpf, String cnpj) {
  }

  private DocumentosValidados validarEPrepararDocumentos(ClienteRequestDTO dto) {
    String cpfRequest = dto.getCpf();
    String cnpjRequest = dto.getCnpj();

    if (cpfRequest != null) {
      cpfRequest = cpfRequest.trim();
      if (cpfRequest.isEmpty()) {
        cpfRequest = null;
      }
    }
    if (cnpjRequest != null) {
      cnpjRequest = cnpjRequest.trim();
      if (cnpjRequest.isEmpty()) {
        cnpjRequest = null;
      }
    }

    if (cpfRequest != null && cnpjRequest != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Não é possível informar CPF e CNPJ ao mesmo tempo.");
    }

    if (cpfRequest == null && cnpjRequest == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "É necessário informar pelo menos um dos campos: CPF ou CNPJ.");
    }
    return new DocumentosValidados(cpfRequest, cnpjRequest);
  }

  @Transactional
  public Cliente create(ClienteRequestDTO clienteRequestDTO) {
    DocumentosValidados docs = validarEPrepararDocumentos(clienteRequestDTO);
    String cpfValidado = docs.cpf();
    String cnpjValidado = docs.cnpj();

    Cliente cliente = new Cliente();
    cliente.setNome(clienteRequestDTO.getNome());
    cliente.setFoto(clienteRequestDTO.getFoto());

    if (clienteRequestDTO.getEmail() != null && !clienteRequestDTO.getEmail().trim().isEmpty()) {
      String emailTrimmed = clienteRequestDTO.getEmail().trim();
      if (clienteRepository.findByEmailIgnoreCase(emailTrimmed).isPresent()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado.");
      }
      cliente.setEmail(emailTrimmed);
    }

    if (clienteRequestDTO.getTelefone() != null && !clienteRequestDTO.getTelefone().trim().isEmpty()) {
      String telefoneTrimmed = clienteRequestDTO.getTelefone().trim();
      if (clienteRepository.findByTelefoneIgnoreCase(telefoneTrimmed).isPresent()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone já cadastrado.");
      }
      cliente.setTelefone(telefoneTrimmed);
    }

    if (cpfValidado != null) {
      if (clienteRepository.findByCpf(cpfValidado).isPresent()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado.");
      }
      cliente.setCpf(cpfValidado);
      cliente.setTipoPessoa(TipoPessoa.FISICA);
      cliente.setCnpj(null);
    } else if (cnpjValidado != null) {
      if (clienteRepository.findByCnpj(cnpjValidado).isPresent()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ já cadastrado.");
      }
      cliente.setCnpj(cnpjValidado);
      cliente.setTipoPessoa(TipoPessoa.JURIDICA);
      cliente.setCpf(null);
    }

    cliente.setLeadScore(clienteRequestDTO.getLeadScore());

    return clienteRepository.save(cliente);
  }

  @Transactional
  public Cliente update(Long id, ClienteRequestDTO clienteRequestDTO) {
    Cliente clienteExistente = clienteRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado com o ID: " + id));

    DocumentosValidados docs = validarEPrepararDocumentos(clienteRequestDTO);
    String cpfValidado = docs.cpf();
    String cnpjValidado = docs.cnpj();

    if (clienteRequestDTO.getNome() != null && !clienteRequestDTO.getNome().trim().isEmpty()) {
      clienteExistente.setNome(clienteRequestDTO.getNome().trim());
    }

    if (clienteRequestDTO.getEmail() != null && !clienteRequestDTO.getEmail().trim().isEmpty()) {
      String emailNovo = clienteRequestDTO.getEmail().trim();
      if (clienteExistente.getEmail() == null || !emailNovo.equalsIgnoreCase(clienteExistente.getEmail())) {
        Optional<Cliente> clienteComMesmoEmail = clienteRepository.findByEmailIgnoreCase(emailNovo);
        if (clienteComMesmoEmail.isPresent() && !clienteComMesmoEmail.get().getId().equals(id)) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado para outro cliente.");
        }
      }
      clienteExistente.setEmail(emailNovo);
    }

    if (clienteRequestDTO.getTelefone() != null && !clienteRequestDTO.getTelefone().trim().isEmpty()) {
      String telefoneNovo = clienteRequestDTO.getTelefone().trim();
      if (clienteExistente.getTelefone() == null || !telefoneNovo.equalsIgnoreCase(clienteExistente.getTelefone())) {
        Optional<Cliente> clienteComMesmoTelefone = clienteRepository.findByTelefoneIgnoreCase(telefoneNovo);
        if (clienteComMesmoTelefone.isPresent() && !clienteComMesmoTelefone.get().getId().equals(id)) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone já cadastrado para outro cliente.");
        }
      }
      clienteExistente.setTelefone(telefoneNovo);
    }

    if (cpfValidado != null) {
      if (clienteExistente.getCpf() == null || !cpfValidado.equals(clienteExistente.getCpf())) {
        Optional<Cliente> clienteComMesmoCpf = clienteRepository.findByCpf(cpfValidado);
        if (clienteComMesmoCpf.isPresent() && !clienteComMesmoCpf.get().getId().equals(id)) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado para outro cliente.");
        }
      }
      clienteExistente.setCpf(cpfValidado);
      clienteExistente.setTipoPessoa(TipoPessoa.FISICA);
      clienteExistente.setCnpj(null);
    } else if (cnpjValidado != null) {
      if (clienteExistente.getCnpj() == null || !cnpjValidado.equals(clienteExistente.getCnpj())) {
        Optional<Cliente> clienteComMesmoCnpj = clienteRepository.findByCnpj(cnpjValidado);
        if (clienteComMesmoCnpj.isPresent() && !clienteComMesmoCnpj.get().getId().equals(id)) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ já cadastrado para outro cliente.");
        }
      }
      clienteExistente.setCnpj(cnpjValidado);
      clienteExistente.setTipoPessoa(TipoPessoa.JURIDICA);
      clienteExistente.setCpf(null);
    }

    if (clienteRequestDTO.getLeadScore() != null) {
      clienteExistente.setLeadScore(clienteRequestDTO.getLeadScore());
    }

    return clienteRepository.save(clienteExistente);
  }

  @Transactional
  public void delete(Long id) {

    if (!clienteRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado com o ID: " + id);
    }
    clienteRepository.deleteById(id);
  }

}
