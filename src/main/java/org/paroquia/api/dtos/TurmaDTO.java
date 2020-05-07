package org.paroquia.api.dtos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.validation.constraints.NotNull;

public class TurmaDTO {

	private static final SimpleDateFormat dateFormat
    = new SimpleDateFormat("yyyy-MM-dd");
	
	private Long id;
	private String descricao;
	private String[] diaSemana;
	private String[] horarios;
	private String dataInicio;
	private String dataFim;
	private Long professorId;
	private Long cursoId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@NotNull(message = "A Descrição não pode ser vazia.")
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String[] getDiaSemana() {
		return diaSemana;
	}
	public void setDiaSemana(String[] diaSemana) {
		this.diaSemana = diaSemana;
	}
	public String[] getHorarios() {
		return horarios;
	}
	public void setHorarios(String[] horarios) {
		this.horarios = horarios;
	}
	@NotNull(message = "Informe uma data de inicio.")
	public String getDataInicio() {
		return dataInicio;
	}
	
	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}
	@NotNull(message = "Informe uma data de término.")
	public String getDataFim() {
		return dataFim;
	}
	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}
	@NotNull(message = "Informe um responsável/professor.")
	public Long getProfessorId() {
		return professorId;
	}
	public void setProfessorId(Long professorId) {
		this.professorId = professorId;
	}
	@NotNull(message = "Informe o curso a qual o turma pertence.")
	public Long getCursoId() {
		return cursoId;
	}
	public void setCursoId(Long cursoId) {
		this.cursoId = cursoId;
	}
	
	
	
	public Date getDataInicioConvertida(String timezone) throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return dateFormat.parse(this.dataInicio);
    }
 
    public void setDataInicioString(Date date, String timezone) {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        this.dataInicio = dateFormat.format(date);
    }
    
    public Date getDataFimConvertida(String timezone) throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return dateFormat.parse(this.dataFim);
    }
 
    public void setDataFimString(Date date, String timezone) {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        this.dataFim = dateFormat.format(date);
    }
}
