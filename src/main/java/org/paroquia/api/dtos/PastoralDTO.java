package org.paroquia.api.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class PastoralDTO {

	private Long id;
	private String nome;
	private String descricao;
	private String email;
	private Long paroquiaId;
	private Long coordenadorId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@NotNull(message = "Nome não pode ser vazia.")
	@Length(min = 5, max = 100, message = "Nome deve conter entre 5 e 100 caracteres.")
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	@NotNull(message = "Email não pode ser vazio.")
	@Email(message = "Email inválido.")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@NotNull(message = "Deve ser selecionado a paroquia da Pastoral.")
	public Long getParoquiaId() {
		return paroquiaId;
	}
	public void setParoquiaId(Long paroquiaId) {
		this.paroquiaId = paroquiaId;
	}
	
	public Long getCoordenadorId() {
		return coordenadorId;
	}
	public void setCoordenadorId(Long coordenadorId) {
		this.coordenadorId = coordenadorId;
	}
	@Override
	public String toString() {
		return "PastoralDTO [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", email=" + email
				+ ", paroquiaId=" + paroquiaId + ", coordenadorId=" + coordenadorId +"]";
	}
	
}
