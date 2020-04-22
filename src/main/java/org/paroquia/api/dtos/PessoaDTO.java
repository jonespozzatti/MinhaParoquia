package org.paroquia.api.dtos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.paroquia.api.entities.Endereco;
import org.paroquia.api.enums.SexoEnum;
import org.paroquia.api.security.enums.PerfilEnum;

public class PessoaDTO {

	private static final SimpleDateFormat dateFormat
    = new SimpleDateFormat("yyyy-MM-dd");
	
	private Long id;
	private String nome;
	private String email;
	private String senha;
	private String cpf;
	private String dataNasc;
	private String telefoneCelular;
	private String telefoneFixo;
	private Endereco endereco;
	private PerfilEnum perfil;
	private SexoEnum sexo;
	private Long responsavel_id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@NotNull(message = "Nome não pode ser vazio.")
	@Length(min = 5, max = 200, message = "Nome deve conter entre 5 e 200 caracteres.")
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@NotNull(message = "Email não pode ser vazio.")
	@Email(message = "Email inválido")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@NotNull(message = "Senha não pode ser vazia.")
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	@NotNull(message = "CPF não pode ser vazio.")
	@CPF (message = "CPF Invalido")
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	@NotNull(message = "Data de nascimento não pode ser vazia.")
	public String getDataNasc() {
		return dataNasc;
	}
	public void setDataNasc(String dataNasc) {
		this.dataNasc = dataNasc;
	}
	public String getTelefoneCelular() {
		return telefoneCelular;
	}
	public void setTelefoneCelular(String telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}
	public String getTelefoneFixo() {
		return telefoneFixo;
	}
	public void setTelefoneFixo(String telefoneFixo) {
		this.telefoneFixo = telefoneFixo;
	}
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public PerfilEnum getPerfil() {
		return perfil;
	}
	public void setPerfil(PerfilEnum perfil) {
		this.perfil = perfil;
	}
	public SexoEnum getSexo() {
		return sexo;
	}
	public void setSexo(SexoEnum sexo) {
		this.sexo = sexo;
	}
	public Long getResponsavel_id() {
		return responsavel_id;
	}
	public void setResponsavel_id(Long responsavel_id) {
		this.responsavel_id = responsavel_id;
	}
	@Override
	public String toString() {
		return "PessoaDTO [id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", cpf=" + cpf
				+ ", dataNasc=" + dataNasc + ", telefoneCelular=" + telefoneCelular + ", telefoneFixo=" + telefoneFixo
				+ ", endereco=" + endereco + ", perfil=" + perfil + ", sexo=" + sexo + ", responsavel_id="
				+ responsavel_id + "]";
	}
	
	public Date getDataNascimetntoConvertida(String timezone) throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return dateFormat.parse(this.dataNasc);
    }
 
    public void setDataNascString(Date date, String timezone) {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        this.dataNasc = dateFormat.format(date);
    }
	
}
