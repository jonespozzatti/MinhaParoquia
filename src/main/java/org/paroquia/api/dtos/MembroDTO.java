package org.paroquia.api.dtos;

import java.util.Optional;

import org.paroquia.api.enums.TipoParticipantePastoral;

public class MembroDTO {

	private Optional<Long> id = Optional.empty();
	private String nome;
	private String telefone;
	private TipoParticipantePastoral tipoParticipantePastoral;
	private Long pastoralId;
	private Long pessoaId;
	
	public MembroDTO() {
	}

	public Optional<Long> getId() {
		return id;
	}

	public void setId(Optional<Long> id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public TipoParticipantePastoral getTipoParticipantePastoral() {
		return tipoParticipantePastoral;
	}
	public void setTipoParticipantePastoral(TipoParticipantePastoral tipoParticipantePastoral) {
		this.tipoParticipantePastoral = tipoParticipantePastoral;
	}
	public Long getPastoralId() {
		return pastoralId;
	}
	public void setPastoralId(Long pastoralId) {
		this.pastoralId = pastoralId;
	}
	public Long getPessoaId() {
		return pessoaId;
	}
	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}
	
	
}
