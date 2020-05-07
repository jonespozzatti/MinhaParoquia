package org.paroquia.api.dtos;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class CursoDTO {


	private Long id;
	private String nome;
	private String descricao;
	private Long paroquiaId;
	
	
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
	@NotNull(message = "O curso deve pertencer a uma paróquia.")
	public Long getParoquiaId() {
		return paroquiaId;
	}
	public void setParoquiaId(Long paroquiaId) {
		this.paroquiaId = paroquiaId;
	}
}
