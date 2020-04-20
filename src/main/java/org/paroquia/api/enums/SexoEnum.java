package org.paroquia.api.enums;

public enum SexoEnum {
	MASCULINO(1, "Masculino"),
	FEMININO(2, "Feminini");
	
	private final Integer valor;
	private final String descricao;

	SexoEnum(Integer valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static 	SexoEnum getSituacao(Integer valor) {
		for (	SexoEnum st : 	SexoEnum.values()) {
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