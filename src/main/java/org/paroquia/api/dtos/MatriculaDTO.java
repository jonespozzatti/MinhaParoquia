package org.paroquia.api.dtos;

import org.paroquia.api.enums.SituacaoMatricula;
import org.paroquia.api.enums.TipoPessoa;

public class MatriculaDTO {

	private Long id;
	private TipoPessoa tipoPessoa;
	private SituacaoMatricula situacaoMatricula;
	private String observacao;
	private Long turmaId;
	private Long pessoaId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}
	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
	public SituacaoMatricula getSituacaoMatricula() {
		return situacaoMatricula;
	}
	public void setSituacaoMatricula(SituacaoMatricula situacaoMatricula) {
		this.situacaoMatricula = situacaoMatricula;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Long getTurmaId() {
		return turmaId;
	}
	public void setTurmaId(Long turmaId) {
		this.turmaId = turmaId;
	}
	public Long getPessoaId() {
		return pessoaId;
	}
	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}
	
	
}
