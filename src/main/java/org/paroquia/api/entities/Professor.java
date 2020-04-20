package org.paroquia.api.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.paroquia.api.enums.EstadoCivil;

@Entity
@DiscriminatorValue(value = "PROFESSOR")
public class Professor extends Pessoa implements Serializable{

	
	private static final long serialVersionUID = -907971852045457655L;
	
	private EstadoCivil estadoCivil;
	private List<Turma> turmas;

	@Column(name = "estadocivil")
	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}
	@OneToMany(mappedBy = "professor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

}
