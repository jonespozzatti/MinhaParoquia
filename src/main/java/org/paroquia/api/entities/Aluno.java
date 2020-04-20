package org.paroquia.api.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
@DiscriminatorValue(value = "Aluno")
public class Aluno extends Pessoa implements Serializable{

	private static final long serialVersionUID = -3707723309593485084L;
	
	private String situacao;
	private List<Turma> turmas;
	
	@Column(name = "situacao")
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	@ManyToMany
	@JoinTable(name= "matricula", 
	        joinColumns = @JoinColumn(name="aluno_id"),
	        inverseJoinColumns = @JoinColumn(name="turma_id"))
	public List<Turma> getTurmas() {
		return turmas;
	}
	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}
	
	

}
