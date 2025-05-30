package com.generation.crm_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.crm_backend.model.Oportunidade;
import com.generation.crm_backend.model.StatusOportunidade;
import com.generation.crm_backend.repository.OportunidadeRepository;

@Service
public class OportunidadeService {

	@Autowired
	private OportunidadeRepository oportunidadeRepository;

	public List<Oportunidade> findAll() {
		return oportunidadeRepository.findAll();
	}

	public Optional<Oportunidade> findById(Long id) {
		return oportunidadeRepository.findById(id);
	}

	public List<Oportunidade> findByStatus(StatusOportunidade status) {
		return oportunidadeRepository.findByStatus(status);
	}

	public List<Oportunidade> findByIdCliente(Long idCliente) {
		return oportunidadeRepository.findByIdCliente(idCliente);
	}

	public List<Oportunidade> findByIdUsuario(Long idUsuario) {
		return oportunidadeRepository.findByIdUsuario(idUsuario);
	}

	public Oportunidade save(Oportunidade oportunidade) {
		return oportunidadeRepository.save(oportunidade);
	}

	public Oportunidade update(Oportunidade oportunidade) {

		if (oportunidade.getId() == null || !oportunidadeRepository.existsById(oportunidade.getId())) {

			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Oportunidade com ID " + oportunidade.getId() + " não encontrada para atualização...");
		}

		return oportunidadeRepository.save(oportunidade);
	}

	public void deleteById(Long id) {
		oportunidadeRepository.deleteById(id);
	}

	public Oportunidade atualizarStatusOportunidade(Long id, StatusOportunidade novoStatus) {
		Optional<Oportunidade> oportunidadeOpt = oportunidadeRepository.findById(id);

		if (oportunidadeOpt.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Oportunidade com ID " + id + " não encontrada!");
		}

		Oportunidade oportunidadeExistente = oportunidadeOpt.get();

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