package org.paroquia.api.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


@Entity
@Table(name = "pastoral")
public class Pastoral implements Serializable {
	
	private static final long serialVersionUID = -6396755950434951961L;

	private Long id;
	private String nome;
	private String descricao;
	private String email;
	private Date dataCriacao;
	private Date dataAtualizacao;
	private Paroquia paroquia;
	private List<PessoaPastoral> participantes= new ArrayList<>(0);

	public Pastoral() {
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "nome", nullable = false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "descricao", nullable = true)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	@Column(name = "email", nullable = true)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "data_criacao", nullable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Column(name = "data_atualizacao", nullable = false)
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name="paroquia_id", nullable = false)
	public Paroquia getParoquia() {
		return paroquia;
	}

	public void setParoquia(Paroquia paroquia) {
		this.paroquia = paroquia;
	}
	
	@OneToMany(mappedBy = "pastoral", fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PessoaPastoral> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<PessoaPastoral> participantes) {
		this.participantes = participantes;
	}

	@PreUpdate
    public void preUpdate() {
        dataAtualizacao = new Date();
    }
     
    @PrePersist
    public void prePersist() {
        final Date atual = new Date();
        dataCriacao = atual;
        dataAtualizacao = atual;
    }

	@Override
	public String toString() {
		return "Pastoral [id=" + id + " nome=" + nome +", descricao=" + descricao + ", email=" + email
				+ ", dataCriacao=" + dataCriacao + ", dataAtualizacao=" + dataAtualizacao 
				+ ", paroquia=" + paroquia + "]";
	}

}