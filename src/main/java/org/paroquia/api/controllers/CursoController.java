package org.paroquia.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.paroquia.api.dtos.CursoDTO;
import org.paroquia.api.entities.Curso;
import org.paroquia.api.entities.Paroquia;
import org.paroquia.api.responses.Response;
import org.paroquia.api.sevices.CursoService;
import org.paroquia.api.sevices.ParoquiaService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/curso")
public class CursoController {

	private static final Logger log = LoggerFactory.getLogger(CursoController.class);
	
	@Autowired
	private CursoService cursoService;
	
	@Autowired
	private ParoquiaService paroquiaService;
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;
	
	public CursoController() {
		
	}
	
	/**
	 * Cadastra um novo curo para a paroquia.
	 * 
	 * @param cursoDTO
	 * @return ResponseEntity<Response<CursoDTO>>
	 */
	@PostMapping
	public ResponseEntity<Response<CursoDTO>> cadastrar(@Valid @RequestBody CursoDTO cursoDTO,
			BindingResult result) {
		
		log.info("Cadastrando um curso: {}", cursoDTO.toString());
		Response<CursoDTO> response = new Response<CursoDTO>();
			
		validarDadosExistentes(cursoDTO, result);
		Curso curso = this.converterDtoParaCurso(cursoDTO);
		
		Optional<Paroquia> paroquia = this.paroquiaService.buscarParoquiaPorId(cursoDTO.getParoquiaId());
		if (!paroquia.isPresent()) {
			result.addError(new ObjectError("curso", "Paróquia não encontrada."));
		} 
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		curso.setParoquia(paroquia.get());
		
		curso = this.cursoService.salvar(curso);
				
		response.setData(this.converterParaCursoDto(curso));
		
		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Atualiza os dados de um curso.
	 * 
	 * @param id
	 * @param cursoDto
	 * @param result
	 * @return ResponseEntity<Response<CursoDTO>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<CursoDTO>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody CursoDTO cursoDTO, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando Curso: {}", cursoDTO.toString());
		Response<CursoDTO> response = new Response<CursoDTO>();

		Optional<Curso> curso = this.cursoService.buscarCursoPorId(id);
		if (!curso.isPresent()) {
			result.addError(new ObjectError("curso", "Curso não encontrada."));
		}

		this.atualizarDadosCurso(curso.get(), cursoDTO, result);

		if (result.hasErrors()) {
			log.error("Erro validando curso: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.cursoService.salvar(curso.get());
		response.setData(this.converterParaCursoDto(curso.get()));

		return ResponseEntity.ok(response);
	}
	
	/**
	 * Remove um curso por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<String>>
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo a postoral id: {}", id);
		Response<String> response = new Response<String>();
		Optional<Curso> curso = this.cursoService.buscarCursoPorId(id);

		if (!curso.isPresent()) {
			log.info("Erro ao remover a curso ID: {} inválido.", id);
			response.getErrors().add("Erro ao remover um curso. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.cursoService.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	/**
	 * Retorna uma curso pelo id.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<CursoDTO>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<CursoDTO>> obter(
			@PathVariable("id") Long id) {
		log.info("Buscando curso por ID: {}", id);
		Response<CursoDTO> response = new Response<CursoDTO>();
		
		Optional<Curso> curso = this.cursoService.buscarCursoPorId(id);
		
		if (!curso.isPresent()) {
			log.info("Curso não encontrada para o ID: {}", id);
			response.getErrors().add("Curso não encontrada para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		
		response.setData(this.converterParaCursoDto(curso.get()));
					
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna a listagem de cursos de um paróquia.
	 * 
	 * @param paroquiaID
	 * @return ResponseEntity<Response<ParoquiaDTO>>
	 */
	@GetMapping(value = "/paroquia/{paroquiaId}")
	public ResponseEntity<Response<List<CursoDTO>>> listarPorParoquiaId(
			@PathVariable("paroquiaId") Long paroquiaId) {
		log.info("Buscando curso por ID da paróquia: {}", paroquiaId);
		Response<List<CursoDTO>> response = new Response<List<CursoDTO>>();

		
		List<Curso> pastorais = this.cursoService.listarCursoPorParoquia(paroquiaId);
		
		if (pastorais.isEmpty()) {
			log.info("Nenhuma curso encontrada para a paróquia de id: {}", paroquiaId);
			response.getErrors().add("Nenhuma curso encontrada para a paróquia de id " + paroquiaId);
			return ResponseEntity.badRequest().body(response);
		}
		
		List<CursoDTO> pastoraisDto = new ArrayList<CursoDTO>(0);
		for (Curso curso : pastorais) {
			pastoraisDto.add(this.converterParaCursoDto(curso));			
		}
		
		response.setData(pastoraisDto);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna a listagem de Cursos de um paróquia paginada.
	 * 
	 * @param paroquiaID
	 * @return ResponseEntity<Response<CursoDTO>>
	 */
	@GetMapping(value = "/paroquia/pag/{paroquiaId}")
	public ResponseEntity<Response<Page<CursoDTO>>> listarCursoPorParoquiaPaginado(
			@PathVariable("paroquiaId") Long paroquiaId,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "nome") String ord,
			@RequestParam(value = "dir", defaultValue = "ASC") String dir) {
		log.info("Buscando curso por ID da paróquia: {}, página: {}", paroquiaId, pag);
		Response<Page<CursoDTO>> response = new Response<Page<CursoDTO>>();

		
		Page<Curso> cursos = this.cursoService
				.listarCursoPorParoquiaPaginado(paroquiaId, PageRequest.of(pag, this.qtdPorPagina, Sort.by(Direction.valueOf(dir),ord)));
		
		if (cursos.isEmpty()) {
			log.info("Nenhuma curso encontrada para a paróquia de id: {}", paroquiaId);
			response.getErrors().add("Nenhuma cursos encontrada para a paróquia de id " + paroquiaId);
			return ResponseEntity.badRequest().body(response);
		}
		
		Page<CursoDTO> cursosDto = (Page<CursoDTO>) cursos
				.map(curso -> this.converterParaCursoDto(curso));
				
		response.setData(cursosDto);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Converte uma DTO para sua respectiva entidade.
	 * 
	 * @param cursoDTO
	 * @return curso
	 */
	private Curso converterDtoParaCurso(CursoDTO cursoDTO) {
		Curso curso = new Curso();
		curso.setId(cursoDTO.getId());
		curso.setNome(cursoDTO.getNome());
		curso.setDescricao(cursoDTO.getDescricao());

		return curso;
	}
	
	/**
	 * Converte uma entidade curso para seu respectivo DTO.
	 * 
	 * @param curso
	 * @return cursoDTO
	 */
	private CursoDTO converterParaCursoDto(Curso curso) {
		CursoDTO dto = new CursoDTO();
		dto.setId(curso.getId());
		dto.setNome(curso.getNome());
		dto.setDescricao(curso.getDescricao());
		dto.setParoquiaId(curso.getParoquia().getId());
		return dto;
	}
	
	
	/**
	 * Verifica se a curso está cadastrada.
	 * 
	 * @param cursoDTO
	 * @param result
	 */
	private void validarDadosExistentes(CursoDTO cursoDTO, BindingResult result) {
		
		this.cursoService.buscarPorNome(cursoDTO.getNome())
		.ifPresent(func -> result.addError(new ObjectError("curso", "Este curso já está cadastrado.")));
		
	}
	
	/**
	 * Atualiza os dados do curso com base nos dados encontrados no DTO.
	 * 
	 * @param curso
	 * @param cursoDTO
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosCurso(Curso curso, CursoDTO cursoDTO, BindingResult result)
			throws NoSuchAlgorithmException {
		curso.setDescricao(cursoDTO.getDescricao());

		if (!curso.getNome().equals(cursoDTO.getNome())) {
			validarDadosExistentes(cursoDTO, result);
		}
		curso.setNome(cursoDTO.getNome());

	}
		
}

