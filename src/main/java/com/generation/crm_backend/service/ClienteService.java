package com.generation.crm_backend.service;

import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
  public Page<Cliente> getAll(int numeroPagina, int tamanhoPagina, String campoOrdenacao, String direcaoOrdenacao) {

    if (!StringUtils.hasText(campoOrdenacao)) {
      campoOrdenacao = "id";
    }

    if (!StringUtils.hasText(direcaoOrdenacao)
        || (!direcaoOrdenacao.equalsIgnoreCase("asc") && !direcaoOrdenacao.equalsIgnoreCase("desc"))) {
      direcaoOrdenacao = "asc";
    }

    Sort sort = Sort.by(Sort.Direction.fromString(direcaoOrdenacao.toUpperCase()), campoOrdenacao);
    Pageable pageable = PageRequest.of(numeroPagina, tamanhoPagina, sort);
    return clienteRepository.findAll(pageable);
  }
  
  @Transactional(readOnly = true)
  public List<Cliente> getAllWithoutPagination() {
    return clienteRepository.findAll();
  }


  @Transactional(readOnly = true)
  public Optional<Cliente> getById(Long id) {
    return clienteRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public Page<Cliente> getAllByNome(String nome, int numeroPagina, int tamanhoPagina, String campoOrdenacao,
      String direcaoOrdenacao) {

    if (!StringUtils.hasText(campoOrdenacao)) {
      campoOrdenacao = "nome";
    }

    if (!StringUtils.hasText(direcaoOrdenacao)
        || (!direcaoOrdenacao.equalsIgnoreCase("asc") && !direcaoOrdenacao.equalsIgnoreCase("desc"))) {
      direcaoOrdenacao = "asc";
    }

    Sort sort = Sort.by(Sort.Direction.fromString(direcaoOrdenacao.toUpperCase()), campoOrdenacao);
    Pageable pageable = PageRequest.of(numeroPagina, tamanhoPagina, sort);
    return clienteRepository.findAllByNomeContainingIgnoreCase(nome, pageable);
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
  public Page<Cliente> getAllByTipoPessoa(TipoPessoa tipoPessoa, int numeroPagina, int tamanhoPagina,
      String campoOrdenacao,
      String direcaoOrdenacao) {
    if (!StringUtils.hasText(campoOrdenacao)) {
      campoOrdenacao = "nome";
    }

    if (!StringUtils.hasText(direcaoOrdenacao)
        || (!direcaoOrdenacao.equalsIgnoreCase("asc") && !direcaoOrdenacao.equalsIgnoreCase("desc"))) {
      direcaoOrdenacao = "asc";
    }

    Sort sort = Sort.by(Sort.Direction.fromString(direcaoOrdenacao.toUpperCase()), campoOrdenacao);
    Pageable pageable = PageRequest.of(numeroPagina, tamanhoPagina, sort);
    return clienteRepository.findAllByTipoPessoa(tipoPessoa, pageable);
  }

  @Transactional(readOnly = true)
  public Cliente getByCpf(String cpf) {
    return clienteRepository.findByCpf(cpf.replaceAll("[^0-9]", ""))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Cliente não encontrado com o CPF: " + cpf));
  }

  @Transactional(readOnly = true)
  public Cliente getByCnpj(String cnpj) {
    return clienteRepository.findByCnpj(cnpj.replaceAll("[^0-9]", ""))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Cliente não encontrado com o CNPJ: " + cnpj));
  }

  @Transactional(readOnly = true)
  public Page<Cliente> getAllByLeadScore(Float leadScore, int numeroPagina, int tamanhoPagina, String campoOrdenacao,
      String direcaoOrdenacao) {
    if (!StringUtils.hasText(campoOrdenacao)) {
      campoOrdenacao = "leadScore";
    }
    if (!StringUtils.hasText(direcaoOrdenacao)
        || (!direcaoOrdenacao.equalsIgnoreCase("asc") && !direcaoOrdenacao.equalsIgnoreCase("desc"))) {
      direcaoOrdenacao = "asc";
    }
    Sort sort = Sort.by(Sort.Direction.fromString(direcaoOrdenacao.toUpperCase()), campoOrdenacao);
    Pageable pageable = PageRequest.of(numeroPagina, tamanhoPagina, sort);
    return clienteRepository.findAllByLeadScore(leadScore, pageable);
  }

  @Transactional(readOnly = true)
  public Page<Cliente> getAllByLeadScoreGreaterThanEqual(Float leadScore, int numeroPagina, int tamanhoPagina,
      String campoOrdenacao, String direcaoOrdenacao) {
    if (!StringUtils.hasText(campoOrdenacao)) {
      campoOrdenacao = "leadScore";
    }
    if (!StringUtils.hasText(direcaoOrdenacao)
        || (!direcaoOrdenacao.equalsIgnoreCase("asc") && !direcaoOrdenacao.equalsIgnoreCase("desc"))) {
      direcaoOrdenacao = "asc";
    }
    Sort sort = Sort.by(Sort.Direction.fromString(direcaoOrdenacao.toUpperCase()), campoOrdenacao);
    Pageable pageable = PageRequest.of(numeroPagina, tamanhoPagina, sort);
    return clienteRepository.findAllByLeadScoreGreaterThanEqual(leadScore, pageable);
  }

  @Transactional(readOnly = true)
  public Page<Cliente> getAllByLeadScoreLessThanEqual(Float leadScore, int numeroPagina, int tamanhoPagina,
      String campoOrdenacao, String direcaoOrdenacao) {
    if (!StringUtils.hasText(campoOrdenacao)) {
      campoOrdenacao = "leadScore";
    }
    if (!StringUtils.hasText(direcaoOrdenacao)
        || (!direcaoOrdenacao.equalsIgnoreCase("asc") && !direcaoOrdenacao.equalsIgnoreCase("desc"))) {
      direcaoOrdenacao = "asc";
    }
    Sort sort = Sort.by(Sort.Direction.fromString(direcaoOrdenacao.toUpperCase()), campoOrdenacao);
    Pageable pageable = PageRequest.of(numeroPagina, tamanhoPagina, sort);
    return clienteRepository.findAllByLeadScoreLessThanEqual(leadScore, pageable);
  }

  private record DocumentosValidados(String cpf, String cnpj) {
  }

  private DocumentosValidados validarEPrepararDocumentos(ClienteRequestDTO dto, boolean isCreate) {
    String cpfRequest = dto.getCpf() != null ? dto.getCpf().replaceAll("[^0-9]", "") : null;
    String cnpjRequest = dto.getCnpj() != null ? dto.getCnpj().replaceAll("[^0-9]", "") : null;

    if (cpfRequest != null && cpfRequest.trim().isEmpty())
      cpfRequest = null;
    if (cnpjRequest != null && cnpjRequest.trim().isEmpty())
      cnpjRequest = null;

    if (isCreate) {
      if (cpfRequest == null && cnpjRequest == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "É necessário informar CPF ou CNPJ para cadastrar um novo cliente.");
      }
    }

    if (cpfRequest != null && cnpjRequest != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Não é possível informar CPF e CNPJ ao mesmo tempo. Escolha um ou outro.");
    }
    return new DocumentosValidados(cpfRequest, cnpjRequest);
  }

  @Transactional
  public Cliente create(ClienteRequestDTO clienteRequestDTO) {
    DocumentosValidados docs = validarEPrepararDocumentos(clienteRequestDTO, true);
    String cpfValidado = docs.cpf();
    String cnpjValidado = docs.cnpj();

    Cliente cliente = new Cliente();
    if (!StringUtils.hasText(clienteRequestDTO.getNome())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do cliente é obrigatório.");
    }
    cliente.setNome(clienteRequestDTO.getNome().trim());
    cliente.setFoto(clienteRequestDTO.getFoto());

    if (StringUtils.hasText(clienteRequestDTO.getEmail())) {
      String emailTrimmed = clienteRequestDTO.getEmail().trim();
      if (clienteRepository.findByEmailIgnoreCase(emailTrimmed).isPresent()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado.");
      }
      cliente.setEmail(emailTrimmed);
    }

    if (StringUtils.hasText(clienteRequestDTO.getTelefone())) {
      String telefoneTrimmed = clienteRequestDTO.getTelefone().trim().replaceAll("[^0-9]", "");
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

    DocumentosValidados docs = validarEPrepararDocumentos(clienteRequestDTO, false);
    String cpfValidado = docs.cpf();
    String cnpjValidado = docs.cnpj();

    if (StringUtils.hasText(clienteRequestDTO.getNome())) {
      clienteExistente.setNome(clienteRequestDTO.getNome().trim());
    }

    if (clienteRequestDTO.getFoto() != null) {
      clienteExistente.setFoto(clienteRequestDTO.getFoto().isEmpty() ? null : clienteRequestDTO.getFoto());
    }

    if (clienteRequestDTO.getEmail() != null) {
      String emailNovo = clienteRequestDTO.getEmail().trim();
      if (emailNovo.isEmpty()) {
        clienteExistente.setEmail(null);
      } else {
        if (clienteExistente.getEmail() == null || !emailNovo.equalsIgnoreCase(clienteExistente.getEmail())) {
          Optional<Cliente> clienteComMesmoEmail = clienteRepository.findByEmailIgnoreCase(emailNovo);
          if (clienteComMesmoEmail.isPresent() && !clienteComMesmoEmail.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado para outro cliente.");
          }
        }
        clienteExistente.setEmail(emailNovo);
      }
    }

    if (clienteRequestDTO.getTelefone() != null) {
      String telefoneNovo = clienteRequestDTO.getTelefone().trim().replaceAll("[^0-9]", "");
      if (telefoneNovo.isEmpty()) {
        clienteExistente.setTelefone(null);
      } else {
        if (clienteExistente.getTelefone() == null || !telefoneNovo.equalsIgnoreCase(clienteExistente.getTelefone())) {
          Optional<Cliente> clienteComMesmoTelefone = clienteRepository.findByTelefoneIgnoreCase(telefoneNovo);
          if (clienteComMesmoTelefone.isPresent() && !clienteComMesmoTelefone.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone já cadastrado para outro cliente.");
          }
        }
        clienteExistente.setTelefone(telefoneNovo);
      }
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
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Cliente não encontrado com o ID: " + id + " para exclusão.");
    }
    clienteRepository.deleteById(id);
  }
}
