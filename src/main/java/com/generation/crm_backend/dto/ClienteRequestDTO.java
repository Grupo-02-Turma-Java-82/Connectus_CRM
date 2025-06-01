package com.generation.crm_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

@Schema(description = "Dados para criar ou atualizar um cliente.")
public class ClienteRequestDTO {

  @NotBlank(message = "O nome é obrigatório!")
  @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
  @Schema(description = "Nome completo ou Razão Social do cliente.", example = "Empresa Exemplo Ltda", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 3, maxLength = 100)
  private String nome;

  @Email(message = "O email deve ser válido!")
  @NotBlank(message = "O email é obrigatório!")
  @Size(max = 100, message = "O e-mail não pode exceder 100 caracteres.")
  @Schema(description = "Endereço de e-mail principal do cliente.", example = "contato@exemplo.com", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
  private String email;

  @URL(message = "O URL da foto deve ser válido.")
  @Size(max = 5000, message = "O URL da foto não pode exceder 5000 caracteres.")
  @Schema(description = "URL para a foto de perfil ou logo do cliente (opcional).", example = "https://example.com/foto_cliente.jpg", maxLength = 5000)
  private String foto;

  @NotBlank(message = "O telefone é obrigatório!")
  @Pattern(regexp = "^((\\(\\d{2}\\)\\s?)|(\\d{2}\\s))?\\d{4,5}-?\\d{4}$|^\\d{10,11}$", message = "O telefone deve estar em um formato válido, como (XX) XXXXX-XXXX, XXXXXXXXXXX ou XX XXXXX-XXXX.")
  @Size(min = 8, max = 20, message = "O telefone deve ter entre 8 e 20 caracteres.")
  @Schema(description = "Número de telefone principal do cliente.", example = "(11) 91234-5678", requiredMode = Schema.RequiredMode.REQUIRED, pattern = "^((\\(\\d{2}\\)\\s?)|(\\d{2}\\s))?\\d{4,5}-?\\d{4}$|^\\d{10,11}$", minLength = 8, maxLength = 20)
  private String telefone;

  @CPF(message = "O CPF deve ser válido.")
  @Schema(description = "CPF do cliente (para pessoa física). Fornecer CPF ou CNPJ.", example = "123.456.789-00")
  private String cpf;

  @CNPJ(message = "O CNPJ deve ser válido.")
  @Schema(description = "CNPJ do cliente (para pessoa jurídica). Fornecer CPF ou CNPJ.", example = "12.345.678/0001-99")
  private String cnpj;

  @NotNull(message = "O lead score é obrigatório!")
  @Min(value = 0, message = "O lead score deve ser no mínimo 0.")
  @Max(value = 10, message = "O lead score deve ser no máximo 10.")
  @Schema(description = "Pontuação do lead, indicando o potencial de conversão.", example = "8.0", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0", maximum = "10")
  private Float leadScore;

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
}