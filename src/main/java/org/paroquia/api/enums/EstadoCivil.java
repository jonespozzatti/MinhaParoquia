package org.paroquia.api.enums;

public enum EstadoCivil {

	CASADO(1,"Casado"),
	DIVORCIADO(3, "Divorciado"),
	SEPARADO(4, "Separado"),
	SOLTEIRO(2,"Solteiro"),
	UNIAO_ESTAVEL(6,"União Estável"),
	VIUVO(5, "Viuvo");

	private final Integer valor;
	private final String descricao;

	EstadoCivil(Integer valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static EstadoCivil getSituacao(Integer valor) {
		for (EstadoCivil st : EstadoCivil.values()) {
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
