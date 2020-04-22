package org.paroquia.api.enums;

public enum TipoParticipantePastoral {

	COORDENADOR(1,"Coordenador"),
	INTEGRANTE(2,"Integrante");
	
	
	private final Integer valor;
	private final String descricao;

	TipoParticipantePastoral(Integer valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static TipoParticipantePastoral getSituacao(Integer valor) {
		for (TipoParticipantePastoral st : TipoParticipantePastoral.values()) {
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
