package com.generation.crm_backend.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_clientes")
public class Cliente {

  public enum TipoPessoa {
    FISICA("Física"),
    JURIDICA("Jurídica");

    private final String descricao;

    TipoPessoa(String descricao) {
      this.descricao = descricao;
    }

    public String getDescricao() {
      return descricao;
    }
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "O nome é obrigatório.")
  @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
  @Column(length = 100, nullable = false)
  private String nome;

  @NotBlank(message = "O e-mail é obrigatório.")
  @Email(message = "O e-mail deve ser válido.")
  @Size(max = 100, message = "O e-mail não pode exceder 100 caracteres.")
  @Column(length = 100, nullable = false, unique = true)
  private String email;

  @Size(max = 5000, message = "O URL da foto não pode exceder 5000 caracteres.")
  @Column(length = 5000)
  private String foto;

  @Pattern(regexp = "^(\\(\\d{2}\\)\\s?)?(\\d{4,5}-?\\d{4})$|^\\d{10,11}$", message = "O telefone deve estar em um formato válido, como (XX) XXXXX-XXXX, XXXXXXXXXXX ou XX XXXXX-XXXX.")
  @Size(min = 8, max = 20, message = "O telefone deve ter entre 8 e 20 caracteres.")
  @Column(length = 20)
  private String telefone;

  @NotNull(message = "O tipo de pessoa é obrigatório.")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private TipoPessoa tipoPessoa;

  @CPF(message = "O CPF deve ser válido.")
  @Column(length = 14, unique = true)
  private String cpf;

  @CNPJ(message = "O CNPJ deve ser válido.")
  @Column(length = 18, unique = true)
  private String cnpj;

  @Min(value = 0, message = "O lead score deve ser no mínimo 0.")
  @Max(value = 10, message = "O lead score deve ser no máximo 10.")
  @Column
  private Float leadScore;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFoto() {
    return foto;
  }

  public void setFoto(String foto) {
    this.foto = foto;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public TipoPessoa getTipoPessoa() {
    return tipoPessoa;
  }

  public void setTipoPessoa(TipoPessoa tipoPessoa) {
    this.tipoPessoa = tipoPessoa;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public Float getLeadScore() {
    return leadScore;
  }

  public void setLeadScore(Float leadScore) {
    this.leadScore = leadScore;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "Cliente{" +
        "id=" + id +
        ", nome='" + nome + '\'' +
        ", email='" + email + '\'' +
        ", tipoPessoa=" + tipoPessoa +
        (cpf != null ? ", cpf='" + cpf + '\'' : "") +
        (cnpj != null ? ", cnpj='" + cnpj + '\'' : "") +
        ", createdAt=" + createdAt +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Cliente cliente = (Cliente) o;
    return id != null && id.equals(cliente.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}