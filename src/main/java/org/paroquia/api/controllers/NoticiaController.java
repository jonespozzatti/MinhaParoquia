package org.paroquia.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.paroquia.api.dtos.CardDTO;
import org.paroquia.api.dtos.NoticiaDTO;
import org.paroquia.api.entities.Noticia;
import org.paroquia.api.entities.Paroquia;
import org.paroquia.api.responses.Response;
import org.paroquia.api.sevices.NoticiaService;
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
@RequestMapping("/api/noticia")
public class NoticiaController {

	
private static final Logger log = LoggerFactory.getLogger(NoticiaController.class);
	
	@Autowired
	private NoticiaService noticiaService;
	
	@Autowired
	private ParoquiaService paroquiaService;
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;
	
	public NoticiaController() {
		
	}
	
	/**
	 * Cadastrar uma nova noticia para a paroquia.
	 * 
	 * @param noticiaDTO
	 * @return ResponseEntity<Response<NoticiaDTO>>
	 */
	@PostMapping
	public ResponseEntity<Response<NoticiaDTO>> cadastrar(@Valid @RequestBody NoticiaDTO noticiaDTO,
			BindingResult result) {
		
		log.info("Cadastrando uma noticia: {}", noticiaDTO.toString());
		Response<NoticiaDTO> response = new Response<NoticiaDTO>();
			
		Noticia noticia = this.converterDtoParaNoticia(noticiaDTO, result);
		
		Optional<Paroquia> paroquia = this.paroquiaService.buscarParoquiaPorId(noticiaDTO.getParoquiaId());
		if (!paroquia.isPresent()) {
			result.addError(new ObjectError("noticia", "Paróquia não encontrada."));
		} 
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		noticia.setParoquia(paroquia.get());
		
		noticia = this.noticiaService.salvar(noticia);
				
		response.setData(this.converterParaNoticiaDto(noticia));
		
		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Atualiza os dados de um noticia.
	 * 
	 * @param id
	 * @param noticiaDto
	 * @param result
	 * @return ResponseEntity<Response<NoticiaDTO>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<NoticiaDTO>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody NoticiaDTO noticiaDTO, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando Noticia: {}", noticiaDTO.toString());
		Response<NoticiaDTO> response = new Response<NoticiaDTO>();

		Optional<Noticia> noticia = this.noticiaService.buscarNoticiaPorId(id);
		if (!noticia.isPresent()) {
			result.addError(new ObjectError("noticia", "Noticia não encontrada."));
		}

		this.atualizarDadosNoticia(noticia.get(), noticiaDTO, result);

		if (result.hasErrors()) {
			log.error("Erro validando noticia: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.noticiaService.salvar(noticia.get());
		response.setData(this.converterParaNoticiaDto(noticia.get()));

		return ResponseEntity.ok(response);
	}
	
	/**
	 * Remove um noticia por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<String>>
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo a postoral id: {}", id);
		Response<String> response = new Response<String>();
		Optional<Noticia> noticia = this.noticiaService.buscarNoticiaPorId(id);

		if (!noticia.isPresent()) {
			log.info("Erro ao remover a noticia ID: {} inválido.", id);
			response.getErrors().add("Erro ao remover um noticia. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.noticiaService.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	/**
	 * Retorna uma noticia pelo id.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<NoticiaDTO>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<NoticiaDTO>> obter(
			@PathVariable("id") Long id) {
		log.info("Buscando noticia por ID: {}", id);
		Response<NoticiaDTO> response = new Response<NoticiaDTO>();
		
		Optional<Noticia> noticia = this.noticiaService.buscarNoticiaPorId(id);
		
		if (!noticia.isPresent()) {
			log.info("Noticia não encontrada para o ID: {}", id);
			response.getErrors().add("Noticia não encontrada para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		
		response.setData(this.converterParaNoticiaDto(noticia.get()));
					
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna a listagem de noticias de um paróquia.
	 * 
	 * @param paroquiaID
	 * @return ResponseEntity<Response<ParoquiaDTO>>
	 */
	@GetMapping(value = "/paroquia/{paroquiaId}")
	public ResponseEntity<Response<List<NoticiaDTO>>> listarPorParoquiaId(
			@PathVariable("paroquiaId") Long paroquiaId) {
		log.info("Buscando noticia por ID da paróquia: {}", paroquiaId);
		Response<List<NoticiaDTO>> response = new Response<List<NoticiaDTO>>();

		
		List<Noticia> noticias = this.noticiaService.listarNoticiaPorParoquia(paroquiaId);
		
		if (noticias.isEmpty()) {
			log.info("Nenhuma noticia encontrada para a paróquia de id: {}", paroquiaId);
			response.getErrors().add("Nenhuma noticia encontrada para a paróquia de id " + paroquiaId);
			return ResponseEntity.badRequest().body(response);
		}
		
		List<NoticiaDTO> pastoraisDto = new ArrayList<NoticiaDTO>(0);
		for (Noticia noticia : noticias) {
			pastoraisDto.add(this.converterParaNoticiaDto(noticia));			
		}
		
		response.setData(pastoraisDto);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna a listagem de avisos de um paróquia ativos.
	 * 
	 * @param paroquiaID
	 * @return ResponseEntity<Response<CardDTO>>
	 */
	@GetMapping(value = "/card/{paroquiaId}")
	public ResponseEntity<Response<List<CardDTO>>> listarAvisoPorParoquiaId(
			@PathVariable("paroquiaId") Long paroquiaId,
			@RequestParam(value = "matches", defaultValue = "true") Boolean matches) {
		log.info("Buscando noticia por ID da paróquia: {}", paroquiaId);
		Response<List<CardDTO>> response = new Response<List<CardDTO>>();

		
		List<Noticia> noticias = this.noticiaService.listarNoticiaPorParoquia(paroquiaId);
		
		if (noticias.isEmpty()) {
			log.info("Nenhuma noticia encontrada para a paróquia de id: {}", paroquiaId);
			response.getErrors().add("Nenhuma noticia encontrada para a paróquia de id " + paroquiaId);
			return ResponseEntity.badRequest().body(response);
		}
		
		List<CardDTO> cardsDTO = new ArrayList<CardDTO>(0);
		for (Noticia noticia : noticias) {
			cardsDTO.add(this.converterParaCardDto(noticia, matches));
			matches = Boolean.TRUE;
		}
		
		response.setData(cardsDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * Retorna a listagem de Noticias de um paróquia paginada.
	 * 
	 * @param paroquiaID
	 * @return ResponseEntity<Response<NoticiaDTO>>
	 */
	@GetMapping(value = "/paroquia/pag/{paroquiaId}")
	public ResponseEntity<Response<Page<NoticiaDTO>>> listarNoticiaPorParoquiaPaginado(
			@PathVariable("paroquiaId") Long paroquiaId,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "nome") String ord,
			@RequestParam(value = "dir", defaultValue = "ASC") String dir) {
		log.info("Buscando noticia por ID da paróquia: {}, página: {}", paroquiaId, pag);
		Response<Page<NoticiaDTO>> response = new Response<Page<NoticiaDTO>>();

		
		Page<Noticia> noticias = this.noticiaService
				.listarNoticiaPorParoquiaPaginado(paroquiaId, PageRequest.of(pag, this.qtdPorPagina, Sort.by(Direction.valueOf(dir),ord)));
		
		if (noticias.isEmpty()) {
			log.info("Nenhuma noticia encontrada para a paróquia de id: {}", paroquiaId);
			response.getErrors().add("Nenhuma noticias encontrada para a paróquia de id " + paroquiaId);
			return ResponseEntity.badRequest().body(response);
		}
		
		Page<NoticiaDTO> noticiasDto = (Page<NoticiaDTO>) noticias
				.map(noticia -> this.converterParaNoticiaDto(noticia));
				
		response.setData(noticiasDto);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Converte uma DTO para sua respectiva entidade.
	 * 
	 * @param noticiaDTO
	 * @return noticia
	 */
	private Noticia converterDtoParaNoticia(NoticiaDTO noticiaDTO, BindingResult result) {
		Noticia noticia = new Noticia();
		noticia.setId(noticiaDTO.getId());
		noticia.setNome(noticiaDTO.getNome());
		noticia.setDescricao(noticiaDTO.getDescricao());
		try {
			noticia.setDataApresentacao(noticiaDTO.getDataApresentacaoConvertida("yyyy-MM-dd"));
		} catch (ParseException e) {
			result.addError(new ObjectError("pessoa", "Data Inválida."));
		}
		noticia.setAtivo(noticiaDTO.getAtivo());


		return noticia;
	}
	
	/**
	 * Converte uma entidade noticia para seu respectivo DTO.
	 * 
	 * @param noticia
	 * @return noticiaDTO
	 */
	private NoticiaDTO converterParaNoticiaDto(Noticia noticia) {
		NoticiaDTO dto = new NoticiaDTO();
		dto.setId(noticia.getId());
		dto.setNome(noticia.getNome());
		dto.setDescricao(noticia.getDescricao());
		dto.setParoquiaId(noticia.getParoquia().getId());
		dto.setDataApresentacaoString(noticia.getDataApresentacao(), "dd/MM/yyyy");
		dto.setAtivo(noticia.getAtivo());
		return dto;
	}
	
	/**
	 * Converte uma entidade noticia para cardDto
	 * @param noticia
	 * @return cardDto
	 */
	private CardDTO converterParaCardDto(Noticia noticia, Boolean matches) {
		CardDTO dto = new CardDTO();
		dto.setTitle(noticia.getNome());
		dto.setDescricao(noticia.getDescricao());
		dto.setRows(1);
		dto.setCols(matches ? 1 : 2);
		return dto;
	}
	
	
	/**
	 * Atualiza os dados do noticia com base nos dados encontrados no DTO.
	 * 
	 * @param noticia
	 * @param noticiaDTO
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosNoticia(Noticia noticia, NoticiaDTO noticiaDTO, BindingResult result)
			throws NoSuchAlgorithmException {

		noticia.setNome(noticiaDTO.getNome());
		noticia.setDescricao(noticiaDTO.getDescricao());
		noticia.setAtivo(noticiaDTO.getAtivo());
		try {
			noticia.setDataApresentacao(noticiaDTO.getDataApresentacaoConvertida("yyyy-MM-dd"));
		} catch (ParseException e) {
			result.addError(new ObjectError("pessoa", "Data Inválida."));
		}
	}
	
}
