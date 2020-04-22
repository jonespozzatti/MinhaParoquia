package org.paroquia.api.dtos;

import javax.validation.constraints.NotNull;

import org.paroquia.api.enums.TipoParticipantePastoral;

public class PessoaPastoralDTO {

	private Long id;
	private TipoParticipantePastoral tipoParticipantePastoral;
	private Long pastoral_id;
	private Long pessoa_id;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TipoParticipantePastoral getTipoParticipantePastoral() {
		return tipoParticipantePastoral;
	}
	public void setTipoParticipantePastoral(TipoParticipantePastoral tipoParticipantePastoral) {
		this.tipoParticipantePastoral = tipoParticipantePastoral;
	}
	@NotNull(message = "Deve ser selecionado uma Pastoral para pessoa.")
	public Long getPastoral_id() {
		return pastoral_id;
	}
	public void setPastoral_id(Long pastoral_id) {
		this.pastoral_id = pastoral_id;
	}
	@NotNull(message = "Deve ser selecionado uma pessoa para pastoral.")
	public Long getPessoa_id() {
		return pessoa_id;
	}
	public void setPessoa_id(Long pessoa_id) {
		this.pessoa_id = pessoa_id;
	}
	@Override
	public String toString() {
		return "PessoaPastoralDTO [id=" + id + ", tipoParticipantePastoral=" + tipoParticipantePastoral
				+ ", pastoral_id=" + pastoral_id + ", pessoa_id=" + pessoa_id + "]";
	}
	
}
