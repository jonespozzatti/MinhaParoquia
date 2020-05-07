package org.paroquia.api.enums;

public enum SituacaoMatricula {

	CONCLUIDO(1,"Concluído"),
	EM_CURSO(2,"Em curso"),
	PENDENDTE(2,"Pendente");
	
	
	private final Integer valor;
	private final String descricao;

	SituacaoMatricula(Integer valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static SituacaoMatricula getSituacao(Integer valor) {
		for (SituacaoMatricula st : SituacaoMatricula.values()) {
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
