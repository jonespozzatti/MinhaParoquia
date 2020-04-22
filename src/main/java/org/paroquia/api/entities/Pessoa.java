package org.paroquia.api.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.paroquia.api.enums.SexoEnum;
import org.paroquia.api.security.enums.PerfilEnum;


@Entity
@Table(name = "pessoa")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", length = 15, discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PESSOA")
public class Pessoa implements Serializable {

	private static final long serialVersionUID = -5754246207015712518L;
	
	private Long id;
	private String nome;
	private String email;
	private String senha;
	private String cpf;
	private Date dataNasc;
	private String telefoneCelular;
	private String telefoneFixo;
	private Endereco endereco;
	private PerfilEnum perfil;
	private Date dataCriacao;
	private Date dataAtualizacao;
	private SexoEnum sexo;
	private Pessoa responsavel;
	private List<PessoaPastoral> pastorais = new ArrayList<>(0);

	public Pessoa() {
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

	@Column(name = "email", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "cpf", nullable = false)
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	@Column(name = "datanasc", nullable = false)
	public Date getDataNasc() {
		return dataNasc;
	}

	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}
	@Column(name = "telefonecelular")
	public String getTelefoneCelular() {
		return telefoneCelular;
	}

	public void setTelefoneCelular(String telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}
	@Column(name = "telefonefixo")
	public String getTelefoneFixo() {
		return telefoneFixo;
	}

	public void setTelefoneFixo(String telefoneFixo) {
		this.telefoneFixo = telefoneFixo;
	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "perfil", nullable = false)
	public PerfilEnum getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilEnum perfil) {
		this.perfil = perfil;
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

	@Column(name = "senha", nullable = false)
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Column(name = "sexo")
	public SexoEnum getSexo() {
		return sexo;
	}

	public void setSexo(SexoEnum sexo) {
		this.sexo = sexo;
	}
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "responsavel_id", nullable = true)
	public Pessoa getResponsavel() {
		return responsavel;
	}
	public void setResponsavel(Pessoa responsavel) {
		this.responsavel = responsavel;
	}

	@OneToMany(mappedBy = "pessoa", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PessoaPastoral> getPastorais() {
		return pastorais;
	}

	public void setPastorais(List<PessoaPastoral> pastorais) {
		this.pastorais = pastorais;
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
		return "Pessoa [id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", cpf=" + cpf
				+ ", dataNasc=" + dataNasc + ", sexo=" + sexo + ", telefoneCelular=" + telefoneCelular + ", telefoneFixo=" + telefoneFixo
				+ ", endereco=" + endereco + ", perfil=" + perfil + ", dataCriacao=" + dataCriacao
				+ ", dataAtualizacao=" + dataAtualizacao + "]";
	}

}