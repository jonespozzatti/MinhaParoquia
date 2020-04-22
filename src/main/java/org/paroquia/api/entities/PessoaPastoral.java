package org.paroquia.api.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.paroquia.api.enums.TipoParticipantePastoral;

@Entity
@Table(name = "pessoapastoral")
public class PessoaPastoral implements Serializable{

	private static final long serialVersionUID = -6330318969625600708L;
	
	private Long id;
	private TipoParticipantePastoral tipoParticipantePastoral;
	private Pastoral pastoral;
	private Pessoa pessoa;
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "tipoparticipantepastoral", nullable = false)
	public TipoParticipantePastoral getTipoParticipantePastoral() {
		return tipoParticipantePastoral;
	}
	public void setTipoParticipantePastoral(TipoParticipantePastoral tipoParticipantePastoral) {
		this.tipoParticipantePastoral = tipoParticipantePastoral;
	}
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "pastoral_id", nullable = false)
	public Pastoral getPastoral() {
		return pastoral;
	}
	public void setPastoral(Pastoral pastoral) {
		this.pastoral = pastoral;
	}
	@ManyToOne(optional = false,  fetch = FetchType.EAGER, cascade = CascadeType.ALL)	
	@JoinColumn(name = "pessoa_id", nullable = false)
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	

}
