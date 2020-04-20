package org.paroquia.api.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.paroquia.api.entities.Endereco;

public class ParoquiaDTO {
	
	private Long id;
	private String razaoSocial;
	private String cnpj;
	private String email;
	private Endereco endereco;
	private String localizacao;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@NotNull(message = "Razão social não pode ser vazia.")
	@Length(min = 5, max = 200, message = "Razão social deve conter entre 5 e 200 caracteres.")
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	@NotNull(message = "CNPJ não pode ser vazio.")
	@CNPJ(message = "CNPJ inválido.")
	public String getCnpj() {
		return cnpj;
	}
	
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	@NotNull(message = "Email não pode ser vazio.")
	@Email(message = "Email inválido.")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public String getLocalizacao() {
		return localizacao;
	}
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	@Override
	public String toString() {
		return "ParoquiaDTO [id=" + id + ", razaoSocial=" + razaoSocial + ", cnpj=" + cnpj + ", email=" + email +"]";
	}
	
	

}
