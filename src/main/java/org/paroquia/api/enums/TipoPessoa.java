package org.paroquia.api.enums;

public enum TipoPessoa {
	
	PROFESSOR(1,"Professor"),
	ALUNO(2,"Aluno");
	
	
	private final Integer valor;
	private final String descricao;

	TipoPessoa(Integer valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static TipoPessoa getSituacao(Integer valor) {
		for (TipoPessoa st : TipoPessoa.values()) {
			if (st.valor.equals(valor)) {
				return st;
			}
		}
		return null;
	}

	public Integer getValor() {
		return valor;
	}

	public String getDescricao() {
		return descricao;
	}

}
