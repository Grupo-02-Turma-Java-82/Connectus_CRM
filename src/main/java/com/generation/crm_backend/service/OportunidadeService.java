package com.generation.crm_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.generation.crm_backend.model.Cliente;
import com.generation.crm_backend.model.Oportunidade;
import com.generation.crm_backend.model.StatusOportunidade;
import com.generation.crm_backend.model.Usuario;
import com.generation.crm_backend.repository.ClienteRepository;
import com.generation.crm_backend.repository.OportunidadeRepository;
import com.generation.crm_backend.repository.UsuarioRepository;

@Service
public class OportunidadeService {

	@Autowired
	private OportunidadeRepository oportunidadeRepository;
	
	@Autowired
    private ClienteRepository clienteRepository; //injeta clienteRepository

	@Autowired
	private UsuarioRepository usuarioRepository; //injeta usuarioRepository

	@Transactional(readOnly = true)
	public List<Oportunidade> findAll() {
		return oportunidadeRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Oportunidade> findById(Long id) {
		return oportunidadeRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public List<Oportunidade> findByStatus(StatusOportunidade status) {
		return oportunidadeRepository.findByStatus(status);
	}

	public List<Oportunidade> findByClienteId(Long idCliente) {
		return oportunidadeRepository.findByClienteId(idCliente);
	}

	public List<Oportunidade> findByUsuarioId(Long idUsuario) {
		return oportunidadeRepository.findByUsuarioId(idUsuario);
	}

	public List<Oportunidade> findAllByTituloContainingIgnoreCase(String titulo) {
		return oportunidadeRepository.findAllByTituloContainingIgnoreCase(titulo);
	}

	//lidar com objetos cliente usuario
	@Transactional
	public Oportunidade save(Oportunidade oportunidadeRecebida) {
		Oportunidade novaOportunidade = new Oportunidade();

		novaOportunidade.setTitulo(oportunidadeRecebida.getTitulo());
		novaOportunidade.setDescricao(oportunidadeRecebida.getDescricao());
		novaOportunidade.setValorEstimado(oportunidadeRecebida.getValorEstimado());

		if (oportunidadeRecebida.getStatus() != null) {
			novaOportunidade.setStatus(oportunidadeRecebida.getStatus());
		}
		if (oportunidadeRecebida.getDataCriacao() != null) {
			novaOportunidade.setDataCriacao(oportunidadeRecebida.getDataCriacao());
		}

		if (oportunidadeRecebida.getCliente() == null || oportunidadeRecebida.getCliente().getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"ID do Cliente é obrigatório para criar a oportunidade.");
		}
		Long clienteId = oportunidadeRecebida.getCliente().getId();
		Cliente clienteAssociado = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Cliente com ID " + clienteId + " não encontrado."));
		novaOportunidade.setCliente(clienteAssociado);

		if (oportunidadeRecebida.getUsuario() == null || oportunidadeRecebida.getUsuario().getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"ID do Usuário é obrigatório para criar a oportunidade.");
		}
		Long usuarioId = oportunidadeRecebida.getUsuario().getId();
		Usuario usuarioAssociado = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Usuário com ID " + usuarioId + " não encontrado."));
		novaOportunidade.setUsuario(usuarioAssociado);

		return oportunidadeRepository.save(novaOportunidade);
	}

	//lidar com obj cliente usuario
	public Oportunidade update(Oportunidade oportunidade) {

		if (oportunidade.getId() == null || !oportunidadeRepository.existsById(oportunidade.getId())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Oportunidade com ID " + oportunidade.getId() + " não encontrada para atualização...");
		}

		Cliente cliente = clienteRepository.findById(oportunidade.getCliente().getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado!"));

		Usuario usuario = usuarioRepository.findById(oportunidade.getUsuario().getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!"));

		oportunidade.setCliente(cliente);
		oportunidade.setUsuario(usuario);

		return oportunidadeRepository.save(oportunidade);
	}

	@Transactional
	public Oportunidade update(Long idDaOportunidade, Oportunidade dadosParaAtualizar) {
		Oportunidade oportunidadeExistente = oportunidadeRepository.findById(idDaOportunidade)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Oportunidade com ID " + idDaOportunidade + " não encontrada para atualização."));

		if (dadosParaAtualizar.getTitulo() != null) {
			oportunidadeExistente.setTitulo(dadosParaAtualizar.getTitulo());
		}
		if (dadosParaAtualizar.getDescricao() != null) {
			oportunidadeExistente.setDescricao(dadosParaAtualizar.getDescricao());
		}
		if (dadosParaAtualizar.getValorEstimado() != null) {
			oportunidadeExistente.setValorEstimado(dadosParaAtualizar.getValorEstimado());
		}
		if (dadosParaAtualizar.getStatus() != null) {
			oportunidadeExistente.setStatus(dadosParaAtualizar.getStatus());
		}

		if (dadosParaAtualizar.getCliente() != null && dadosParaAtualizar.getCliente().getId() != null) {
			Long novoClienteId = dadosParaAtualizar.getCliente().getId();
			if (oportunidadeExistente.getCliente() == null
					|| !oportunidadeExistente.getCliente().getId().equals(novoClienteId)) {
				Cliente novoClienteAssociado = clienteRepository.findById(novoClienteId)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
								"Novo Cliente com ID " + novoClienteId + " não encontrado."));
				oportunidadeExistente.setCliente(novoClienteAssociado);
			}
		}

		if (dadosParaAtualizar.getUsuario() != null && dadosParaAtualizar.getUsuario().getId() != null) {
			Long novoUsuarioId = dadosParaAtualizar.getUsuario().getId();
			if (oportunidadeExistente.getUsuario() == null
					|| !oportunidadeExistente.getUsuario().getId().equals(novoUsuarioId)) {
				Usuario novoUsuarioAssociado = usuarioRepository.findById(novoUsuarioId)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
								"Novo Usuário com ID " + novoUsuarioId + " não encontrado."));
				oportunidadeExistente.setUsuario(novoUsuarioAssociado);
			}
		}

		return oportunidadeRepository.save(oportunidadeExistente);
	}

	@Transactional
	public void deleteById(Long id) {
		if (!oportunidadeRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Oportunidade com ID " + id + " não encontrada para exclusão!");
		}
		oportunidadeRepository.deleteById(id);
	}

	@Transactional
	public Oportunidade atualizarStatusOportunidade(Long id, StatusOportunidade novoStatus) {
		Oportunidade oportunidadeExistente = oportunidadeRepository.findById(id)
				.orElseThrow(
						() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Oportunidade com ID " + id + " não encontrada!"));

		if (oportunidadeExistente.getStatus() == StatusOportunidade.GANHA && novoStatus == StatusOportunidade.NOVA) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Não é possível reabrir uma oportunidade que já foi GANHA como NOVA.");
		}
		if (oportunidadeExistente.getStatus() == StatusOportunidade.GANHA && novoStatus == StatusOportunidade.PERDIDA) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Não é possível mudar uma oportunidade GANHA para PERDIDA.");
		}
		if (oportunidadeExistente.getStatus() == StatusOportunidade.NOVA
				&& !(novoStatus == StatusOportunidade.EM_NEGOCIACAO || novoStatus == StatusOportunidade.PERDIDA)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Oportunidades NOVAS só podem ir para EM_NEGOCIACAO ou PERDIDA.");
		}
		if (oportunidadeExistente.getStatus() == StatusOportunidade.EM_NEGOCIACAO
				&& !(novoStatus == StatusOportunidade.GANHA || novoStatus == StatusOportunidade.PERDIDA
						|| novoStatus == StatusOportunidade.ARQUIVADA)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Oportunidades EM_NEGOCIACAO só podem ir para GANHA, PERDIDA ou ARQUIVADA.");
		}

		oportunidadeExistente.setStatus(novoStatus);
		return oportunidadeRepository.save(oportunidadeExistente);
	}
}