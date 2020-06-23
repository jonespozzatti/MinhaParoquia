package org.paroquia.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.paroquia.api.dtos.TurmaDTO;
import org.paroquia.api.entities.Curso;
import org.paroquia.api.entities.Matricula;
import org.paroquia.api.entities.Pessoa;
import org.paroquia.api.entities.Turma;
import org.paroquia.api.enums.TipoPessoa;
import org.paroquia.api.responses.Response;
import org.paroquia.api.sevices.CursoService;
import org.paroquia.api.sevices.MatriculaService;
import org.paroquia.api.sevices.PessoaService;
import org.paroquia.api.sevices.TurmaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/turma")
public class TurmaController {
	private static final Logger log = LoggerFactory.getLogger(TurmaController.class);
	
	@Autowired
	private TurmaService turmaService;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private CursoService cursoService;
	
	@Autowired
	private MatriculaService matriculaService;
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;
	
	/**
	 * Cadastra uma nova turma para a turma.
	 * 
	 * @param turmaDTO
	 * @return ResponseEntity<Response<TurmaDTO>>
	 */
	@PostMapping
	public ResponseEntity<Response<TurmaDTO>> cadastrar(@Valid @RequestBody TurmaDTO turmaDTO,
			BindingResult result) {
		
		log.info("Cadastrando um turma: {}", turmaDTO.toString());
		Response<TurmaDTO> response = new Response<TurmaDTO>();
		Optional<Pessoa> professor=null;
		Optional<Curso> curso = null;
		Turma turma = null;
		Matricula matricula = null;
		
		if(!result.hasErrors()) {
			professor = this.pessoaService.buscarPorId(turmaDTO.getProfessorId());
			 
			curso = this.cursoService.buscarCursoPorId(turmaDTO.getCursoId());
			
			validarDadosExistentes(professor, curso, result);
		}
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		turma = this.converterDtoParaTurma(turmaDTO, result);		
		
		turma.setCurso(curso.get());
		
		matricula = popularMatricula(turma, professor);
		
		matricula = matriculaService.salvar(matricula);
		
		response.setData(this.converterParaTurmaDto(turma, matricula));
		
		return ResponseEntity.ok(response);
	}	


	/**
	 * Atualiza os dados de um turma.
	 * 
	 * @param id
	 * @param turmaDto
	 * @param result
	 * @return ResponseEntity<Response<TurmaDTO>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<TurmaDTO>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody TurmaDTO turmaDTO, BindingResult result) {
		log.info("Atualizando Turma: {}", turmaDTO.toString());
		Response<TurmaDTO> response = new Response<TurmaDTO>();
		Optional<Pessoa> professor=null;
		Optional<Curso> curso = null;
		Optional<Turma> turma = null;
		Optional<Matricula> matricula = null;

		turma = this.turmaService.buscarTurmaPorId(id);
		if (!turma.isPresent()) {
			result.addError(new ObjectError("turma", "Turma não encontrada."));
		}
		
		if(!result.hasErrors()) {
			professor = this.pessoaService.buscarPorId(turmaDTO.getProfessorId());
			 
			curso = this.cursoService.buscarCursoPorId(turmaDTO.getCursoId());
			
			validarDadosExistentes(professor, curso, result);
		}

		if (result.hasErrors()) {
			log.error("Erro validando turma: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		matricula = matriculaService.buscarPorPessoaAndTurma(professor.get().getId(), turma.get().getId());
		
		atualizarDadosTurma(turmaDTO, result, professor.get(), curso.get(), turma.get(), matricula.get());
		

		response.setData(this.converterParaTurmaDto(turma.get(), matriculaService.salvar(matricula.get())));

		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Retorna uma turma pelo id.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<TurmaDTO>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<TurmaDTO>> obter(
			@PathVariable("id") Long id) {
		log.info("Buscando turma por ID: {}", id);
		Response<TurmaDTO> response = new Response<TurmaDTO>();
		
		Optional<Turma> turma = this.turmaService.buscarTurmaPorId(id);
		
		if (!turma.isPresent()) {
			log.info("Turma não encontrada para o ID: {}", id);
			response.getErrors().add("Turma não encontrada para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		List<Matricula> matricula = matriculaService.listarPorTurmaPorTipoPessoa(TipoPessoa.PROFESSOR, turma.get().getId());

		response.setData(this.converterParaTurmaDto(turma.get(), matricula.get(0)));
					
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna a listagem de turmas de um curso.
	 * 
	 * @param cursoID
	 * @return ResponseEntity<Response<List<TurmaDTO>>>
	 */
	@GetMapping(value = "/curso/{cursoId}")
	public ResponseEntity<Response<List<TurmaDTO>>> listarPorCursoId(
			@PathVariable("cursoId") Long cursoId) {
		log.info("Buscando turma por ID do curso: {}", cursoId);
		Response<List<TurmaDTO>> response = new Response<List<TurmaDTO>>();

		
		List<Turma> turmas = this.turmaService.listarTurmaPorCurso(cursoId);
		
		if (turmas.isEmpty()) {
			log.info("Nenhuma turma encontrada para o curso de id: {}", cursoId);
			response.getErrors().add("Nenhuma turma encontrada para o curso de id " + cursoId);
			return ResponseEntity.badRequest().body(response);
		}
		
		List<TurmaDTO> turmasDto = new ArrayList<TurmaDTO>(0);
		for (Turma turma : turmas) {
			List<Matricula> matricula = matriculaService.listarPorTurmaPorTipoPessoa(TipoPessoa.PROFESSOR, turma.getId());
			turmasDto.add(this.converterParaTurmaDto(turma, matricula.get(0)));			
		}
		
		response.setData(turmasDto);
		return ResponseEntity.ok(response);
	}
	

	/**
	 * Retorna a listagem de Turmas de um curso paginada.
	 * 
	 * @param cursoId
	 * @return ResponseEntity<Response<TurmaDTO>>
	 */
	@GetMapping(value = "/curso/pag/{cursoId}")
	public ResponseEntity<Response<Page<TurmaDTO>>> listarTurmasPorCursoPaginado(
			@PathVariable("cursoId") Long cursoId,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "descricao") String ord,
			@RequestParam(value = "dir", defaultValue = "ASC") String dir) {
		log.info("Buscando turmas por ID do curso: {}, página: {}", cursoId, pag);
		Response<Page<TurmaDTO>> response = new Response<Page<TurmaDTO>>();

		
		Page<Turma> turmas = this.turmaService
				.listarTurmasPorCursoPaginado(cursoId, PageRequest.of(pag, this.qtdPorPagina, Sort.by(Direction.valueOf(dir),ord)));
		
		if (turmas.isEmpty()) {
			log.info("Nenhuma turma encontrada para o curso de id: {}", cursoId);
			response.getErrors().add("Nenhuma turma encontrada para o curso de id " + cursoId);
			return ResponseEntity.badRequest().body(response);
		}
		
		Page<TurmaDTO> turmasDto = (Page<TurmaDTO>) turmas
				.map(turma -> this.converterParaTurmaDto(turma));
				
		response.setData(turmasDto);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Converte uma DTO para sua respectiva entidade.
	 * 
	 * @param turmaDTO
	 * @return turma
	 */
	private Turma converterDtoParaTurma(TurmaDTO turmaDTO, BindingResult result) {
		Turma turma = new Turma();
		turma.setId(turmaDTO.getId());
		try {
			turma.setDataInicio(turmaDTO.getDataInicioConvertida("yyyy-MM-dd"));
			turma.setDataFim(turmaDTO.getDataFimConvertida("yyyy-MM-dd"));
		} catch (ParseException e) {
			result.addError(new ObjectError("turma", "Data inicio Inválida."));
		}
		turma.setDescricao(turmaDTO.getDescricao());
		turma.setDiaSemana(turmaDTO.getDiaSemana());
		turma.setHorarios(turmaDTO.getHorarios());
		

		return turma;
	}
	
	/**
	 * Converte uma entidade turma para seu respectivo DTO.
	 * 
	 * @param turma
	 * @return turmaDTO
	 */
	private TurmaDTO converterParaTurmaDto(Turma turma, Matricula matricula) {
		TurmaDTO dto = new TurmaDTO();
		dto.setId(turma.getId());
		dto.setDataInicioString(turma.getDataInicio(), "yyyy-MM-dd");
		dto.setDataFimString(turma.getDataFim(), "yyyy-MM-dd");
		dto.setDescricao(turma.getDescricao());
		dto.setProfessorId(matricula.getPessoa().getId());
		dto.setDiaSemana(turma.getDiaSemana());
		dto.setHorarios(turma.getHorarios());
		dto.setCursoId(turma.getCurso().getId());
		return dto;
	}
	
	/**
	 * Converte uma entidade turma para seu respectivo DTO.
	 * 
	 * @param turma
	 * @return turmaDTO
	 */
	private TurmaDTO converterParaTurmaDto(Turma turma) {
		TurmaDTO dto = new TurmaDTO();
		dto.setId(turma.getId());
		dto.setDataInicioString(turma.getDataInicio(), "yyyy-MM-dd");
		dto.setDataFimString(turma.getDataFim(), "yyyy-MM-dd");
		dto.setDescricao(turma.getDescricao());
		dto.setDiaSemana(turma.getDiaSemana());
		dto.setHorarios(turma.getHorarios());
		dto.setCursoId(turma.getCurso().getId());
		return dto;
	}
	
	
	/**
	 * Verifica se a turma está cadastrada.
	 * 
	 * @param turmaDTO
	 * @param result
	 */
	private void validarDadosExistentes(Optional<Pessoa> professor, Optional<Curso> curso, BindingResult result) {
		
		if (!professor.isPresent()) {
			result.addError(new ObjectError("turma", "Professor não encontrado."));
		} 
		if (!curso.isPresent()) {
			result.addError(new ObjectError("turma", "Curso não encontrado."));
		} 
	}
	
	/**
	 * Atualiza os dados do turma com base nos dados encontrados no DTO.
	 * 
	 * @param turma
	 * @param turmaDTO
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosTurma(TurmaDTO turmaDTO, BindingResult result, Pessoa professor,
			Curso curso, Turma turma, Matricula matricula) {
		try {
			turma.setDataInicio(turmaDTO.getDataInicioConvertida("yyyy-MM-dd"));
			turma.setDataFim(turmaDTO.getDataFimConvertida("yyyy-MM-dd"));
		} catch (ParseException e) {
			result.addError(new ObjectError("turma", "Data inicio Inválida."));
		}
		turma.setDescricao(turmaDTO.getDescricao());
		turma.setDiaSemana(turmaDTO.getDiaSemana());
		turma.setHorarios(turmaDTO.getHorarios());
		
		turma.setCurso(curso);
		
		matricula.setPessoa(professor);
		matricula.setTurma(turma);
	}
	
	/**
	 * Cria uma entidade Matricula.
	 * 
	 * @param turma
	 * @return turmaDTO
	 */
	private Matricula popularMatricula(Turma turma, Optional<Pessoa> professor) {
		Matricula matricula = new Matricula();
		matricula.setPessoa(professor.get());
		matricula.setTurma(turma);
		matricula.setTipoPessoa(TipoPessoa.PROFESSOR);
		return matricula;
	}

}
