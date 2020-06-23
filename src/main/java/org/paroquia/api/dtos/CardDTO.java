package org.paroquia.api.dtos;

public class CardDTO {

	
	private String title;
	private String descricao;
	private Integer cols;
	private Integer rows;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getCols() {
		return cols;
	}
	public void setCols(Integer cols) {
		this.cols = cols;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	
	
}
