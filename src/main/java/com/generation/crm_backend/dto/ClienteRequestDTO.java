package com.generation.crm_backend.dto;

import com.generation.crm_backend.model.Cliente.TipoPessoa;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ClienteRequestDTO {
  @NotBlank(message = "O nome é obrigatório!")
  private String nome;

  @Email(message = "O email deve ser válido!")
  @NotBlank(message = "O email é obrigatório!")
  private String email;

  private String foto;

  @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", message = "O telefone deve estar em um formato válido, como (XX) XXXXX-XXXX ou XXXXXXXXXXX.")
  @NotBlank(message = "O telefone é obrigatório!")
  private String telefone;

  @NotNull(message = "O tipo de pessoa é obrigatório!")
  private TipoPessoa tipoPessoa;

  private String cpf;

  private String cnpj;

  @NotNull(message = "O lead score é obrigatório!")
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
}
