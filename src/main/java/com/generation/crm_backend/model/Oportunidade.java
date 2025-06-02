package com.generation.crm_backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_oportunidades")
public class Oportunidade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "O ID do cliente é obrigatório!")
	private Long idCliente;

	@NotNull(message = "O ID do usuário é obrigatório!")
	private Long idUsuario;

	@Column(length = 100, nullable = false)
	@NotBlank(message = "O título da oportunidade é obrigatório!")
	@Size(min = 5, max = 100, message = "O atributo título deve ter no minimo 5 e no máximo 100 caracteres.")
	private String titulo;

	@Column(length = 1000)
	@Size(max = 1000, message = "A descrição deve ter no máximo máximo 1000 caracteres.")
	private String descricao;

	@NotNull(message = "O valor estimado é obrigatório!")
	@Column(name = "valor_estimado", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorEstimado;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O status da oportunidade é obrigatório!")
	@Column(name = "status_oportunidade", nullable = false, length = 50)
	private StatusOportunidade status;

	@NotNull(message = "A data de criação é obrigatória!")
	@Column(name = "data_criacao", nullable = false)
	private LocalDate dataCriacao;
	
    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "cliente")
    private Cliente cliente;

	public Oportunidade() {
		this.dataCriacao = LocalDate.now(); // data atual do sistema
		this.status = StatusOportunidade.NOVA;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValorEstimado() {
		return valorEstimado;
	}

	public void setValorEstimado(BigDecimal valorEstimado) {
		this.valorEstimado = valorEstimado;
	}

	public StatusOportunidade getStatus() {
		return status;
	}

	public void setStatus(StatusOportunidade status) {
		this.status = status;
	}

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
}
