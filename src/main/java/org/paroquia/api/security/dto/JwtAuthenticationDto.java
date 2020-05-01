package org.paroquia.api.security.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

public class JwtAuthenticationDto {

	private String cpf;
	private String senha;

	public JwtAuthenticationDto() {
	}

	@NotNull(message = "CPF não pode ser vazio.")
	@CPF (message = "CPF Invalido")
	public String getCpf() {
		return cpf;
	}

	public void setcpf(String cpf) {
		this.cpf = cpf;
	}

	@NotNull(message = "CPF não pode ser vazio.")
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "JwtAuthenticationRequestDto [cpf=" + cpf + ", senha=" + senha + "]";
	}

}