package com.generation.crm_backend.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Representa um cliente no sistema CRM, podendo ser pessoa física ou jurídica.")
public class Cliente {

  @Schema(description = "Tipo de pessoa do cliente (Física ou Jurídica).")
  public enum TipoPessoa {
    @Schema(description = "Pessoa Física")
    FISICA("Física"),
    @Schema(description = "Pessoa Jurídica")
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
  @Schema(description = "Identificador único do cliente.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @NotBlank(message = "O nome é obrigatório.")
  @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
  @Column(length = 100, nullable = false)
  @Schema(description = "Nome completo ou Razão Social do cliente.", example = "João da Silva / Empresa XYZ Ltda", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 3, maxLength = 100)
  private String nome;

  @NotBlank(message = "O e-mail é obrigatório.")
  @Email(message = "O e-mail deve ser válido.")
  @Size(max = 100, message = "O e-mail não pode exceder 100 caracteres.")
  @Column(length = 100, nullable = false, unique = true)
  @Schema(description = "Endereço de e-mail principal do cliente.", example = "contato@empresaxyz.com", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
  private String email;

  @Size(max = 5000, message = "O URL da foto não pode exceder 5000 caracteres.")
  @Column(length = 5000)
  @Schema(description = "URL para a foto de perfil ou logo do cliente.", example = "https://example.com/foto_cliente.jpg", maxLength = 5000)
  private String foto;

  @Pattern(regexp = "^((\\(\\d{2}\\)\\s?)|(\\d{2}\\s))?\\d{4,5}-?\\d{4}$|^\\d{10,11}$", message = "O telefone deve estar em um formato válido, como (XX) XXXXX-XXXX, XXXXXXXXXXX ou XX XXXXX-XXXX.")
  @Size(min = 8, max = 20, message = "O telefone deve ter entre 8 e 20 caracteres.")
  @Column(length = 20)
  @Schema(description = "Número de telefone principal do cliente.", example = "(11) 98765-4321", pattern = "^((\\(\\d{2}\\)\\s?)|(\\d{2}\\s))?\\d{4,5}-?\\d{4}$|^\\d{10,11}$", minLength = 8, maxLength = 20)
  private String telefone;

  @NotNull(message = "O tipo de pessoa é obrigatório.")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  @Schema(description = "Define se o cliente é Pessoa Física ou Jurídica.", example = "FISICA", requiredMode = Schema.RequiredMode.REQUIRED)
  private TipoPessoa tipoPessoa;

  @CPF(message = "O CPF deve ser válido.")
  @Column(length = 14, unique = true)
  @Schema(description = "Cadastro de Pessoa Física (CPF) do cliente. Obrigatório se tipoPessoa for FISICA. Deve ser único. Será armazenado apenas com números.", example = "12345678900")
  private String cpf;

  @CNPJ(message = "O CNPJ deve ser válido.")
  @Column(length = 18, unique = true)
  @Schema(description = "Cadastro Nacional da Pessoa Jurídica (CNPJ) do cliente. Obrigatório se tipoPessoa for JURIDICA. Deve ser único. Será armazenado apenas com números.", example = "12345678000199")
  private String cnpj;

  @Min(value = 0, message = "O lead score deve ser no mínimo 0.")
  @Max(value = 10, message = "O lead score deve ser no máximo 10.")
  @Column
  @Schema(description = "Pontuação do lead, indicando o potencial de conversão do cliente.", example = "7.5", minimum = "0", maximum = "10")
  private Float leadScore;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  @Schema(description = "Data e hora de criação do registro do cliente.", example = "2024-05-30T10:15:30", accessMode = Schema.AccessMode.READ_ONLY)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  @Schema(description = "Data e hora da última atualização do registro do cliente.", example = "2024-05-31T14:30:00", accessMode = Schema.AccessMode.READ_ONLY)
  private LocalDateTime updatedAt;

  // Getters e Setters

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
    this.cpf = (cpf != null && !cpf.trim().isEmpty()) ? cpf.replaceAll("[^0-9]", "") : null;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = (cnpj != null && !cnpj.trim().isEmpty()) ? cnpj.replaceAll("[^0-9]", "") : null;
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

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public String toString() {
    return "Cliente{" +
        "id=" + id +
        ", nome='" + nome + '\'' +
        ", email='" + email + '\'' +
        ", tipoPessoa=" + tipoPessoa +
        ", cpf='" + cpf + '\'' +
        ", cnpj='" + cnpj + '\'' +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
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

    return Objects.hash(id);
  }
}