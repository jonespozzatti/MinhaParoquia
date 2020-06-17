package org.paroquia.api.dtos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.validation.constraints.NotNull;

public class NoticiaDTO {
	
	private static final SimpleDateFormat dateFormat
    = new SimpleDateFormat("yyyy-MM-dd");
	
	
	private Long id;
	private String nome;
	private String descricao;
	private Long paroquiaId;
	private String dataApresentacao;
	private Boolean ativo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@NotNull(message = "Nome não pode ser vazia.")
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@NotNull(message = "Descrição não pode ser vazia.")
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Long getParoquiaId() {
		return paroquiaId;
	}
	public void setParoquiaId(Long paroquiaId) {
		this.paroquiaId = paroquiaId;
	}
	public String getDataApresentacao() {
		return dataApresentacao;
	}
	public void setDataApresentacao(String dataApresentacao) {
		this.dataApresentacao = dataApresentacao;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Date getDataApresentacaoConvertida(String timezone) throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return dateFormat.parse(this.dataApresentacao);
    }
 
    public void setDataApresentacaoString(Date date, String timezone) {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        this.dataApresentacao = dateFormat.format(date);
    }
	
}
