package org.paroquia.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.paroquia.api.dtos.MembroDTO;
import org.paroquia.api.dtos.PessoaPastoralDTO;
import org.paroquia.api.entities.Pastoral;
import org.paroquia.api.entities.Pessoa;
import org.paroquia.api.entities.PessoaPastoral;
import org.paroquia.api.enums.TipoParticipantePastoral;
import org.paroquia.api.responses.Response;
import org.paroquia.api.sevices.PastoralService;
import org.paroquia.api.sevices.PessoaPastoralService;
import org.paroquia.api.sevices.PessoaService;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/pessoapastoral")
public class PessoaPastoralController {

	private static final Logger log = LoggerFactory.getLogger(PastoralController.class);
	
	@Autowired
	private PessoaPastoralService pessoaPastoralService;
	
	@Autowired
	private PastoralService pastoralService;
		
	@Autowired
	private PessoaService pessoaService;
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;
	
	/**
	 * Cadastra uma nova pastoral de um paróquia.
	 * 
	 * @param PessoaPastoralDTO pessoaPastoralDTO
	 * @return ResponseEntity<Response<PessoaPastoralDTO>>
	 */
	@PostMapping
	public ResponseEntity<Response<PessoaPastoralDTO>> incluirPessoaPastoral(@Valid @RequestBody PessoaPastoralDTO pessoaPastoralDTO,
			BindingResult result) {
		
		log.info("Incluindo Pessoa a Pastoral: {}", pessoaPastoralDTO.toString());
		Response<PessoaPastoralDTO> response = new Response<PessoaPastoralDTO>();
		
		Optional<Pessoa> pessoa = this.pessoaService.buscarPorId(pessoaPastoralDTO.getPessoa_id());
		Optional<Pastoral> pastoral = this.pastoralService.buscarPorId(pessoaPastoralDTO.getPastoral_id());
		
		validarDadosParaIncluir(result, pessoa, pastoral);
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		PessoaPastoral pessoaPastoral = this.converterDtoParaPessoaPastoral(pessoaPastoralDTO, pastoral, pessoa);
		
		
		pessoaPastoral = this.pessoaPastoralService.salvar(pessoaPastoral);
		
		response.setData(this.converterParaPessoaPastoralDto(pessoaPastoral));
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Atualiza os dados de um membro da pastoral.
	 * 
	 * @param id
	 * @param PessoaPastoralDTO pessoaPastoralDTO
	 * @param result
	 * @return ResponseEntity<Response<PessoaPastoralDTO>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<PessoaPastoralDTO>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody PessoaPastoralDTO pessoaPastoralDTO, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando Pessoa Pastoral: {}", pessoaPastoralDTO.toString());
		Response<PessoaPastoralDTO> response = new Response<PessoaPastoralDTO>();

		Optional<PessoaPastoral> pessoaPastoral = this.pessoaPastoralService.buscarPorId(id);
		if (!pessoaPastoral.isPresent()) {
			result.addError(new ObjectError("pessoaPastoral", "PessoaPastoral não encontrada."));
		}

		pessoaPastoral.get().setTipoParticipantePastoral(pessoaPastoralDTO.getTipoParticipantePastoral());

		if (result.hasErrors()) {
			log.error("Erro validando pessoaPastoral: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.pessoaPastoralService.salvar(pessoaPastoral.get());
		response.setData(this.converterParaPessoaPastoralDto(pessoaPastoral.get()));

		return ResponseEntity.ok(response);
	}

	/**
	 * Remove uma pessoa da postoral por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<String>>
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo uma pessoa da postoral: {}", id);
		Response<String> response = new Response<String>();
		Optional<PessoaPastoral> pessoaPastoral = this.pessoaPastoralService.buscarPorId(id);

		if (!pessoaPastoral.isPresent()) {
			log.info("Erro ao remover uma pessoa da pastoral ID: {} ser inválido.", id);
			response.getErrors().add("Erro ao remover uma pessoa da pastoral. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}else if (TipoParticipantePastoral.COORDENADOR.equals(pessoaPastoral.get().getTipoParticipantePastoral())) {
			response.getErrors().add("Erro: Altere sua função antes de excluir.");
			return ResponseEntity.badRequest().body(response);
		}

		this.pessoaPastoralService.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}
	

	/**
	 * Retorna a listagem de pessoas de uma pastoral.
	 * 
	 * @param pastoralID
	 * @return ResponseEntity<Response<PessoaPastoralDTO>>
	 */
	@GetMapping(value = "/pastoral/{pastoralId}")
	public ResponseEntity<Response<List<PessoaPastoralDTO>>> listarPessoasPorPastoral(
			@PathVariable("pastoralId") Long pastoralId) {
		log.info("Buscando pessoapastoral por ID da pastoral: {}", pastoralId);
		Response<List<PessoaPastoralDTO>> response = new Response<List<PessoaPastoralDTO>>();

		
		List<PessoaPastoral> listaPessoasPastoral = this.pessoaPastoralService.listarPorPastoral(pastoralId);
		
		response.setData(this.converterParaListaPessoaPastoralDto(listaPessoasPastoral));
		
		return ResponseEntity.ok(response);
	}
	/**
	 * Retorna a listagem de pessoas que não pertencem a pastoral.
	 * 
	 * @param pastoralID
	 * @return ResponseEntity<Response<PessoaPastoralDTO>>
	 * @throws JsonProcessingException 
	 */
	@GetMapping(value = "/naopastoral/{pastoralId}")
	public ResponseEntity<String> listarPessoasNaoPertencemPastoral(
			@PathVariable("pastoralId") Long pastoralId) throws JsonProcessingException {
		log.info("Buscando pessoas que não pertence a pastoral de ID: {}", pastoralId);
		
		List<Pessoa> listaPessoas = this.pessoaService.listarPessoasNaoPertencemPastoral(pastoralId);
		
		List<MembroDTO> membrosDto= new ArrayList<MembroDTO>();
		
		for (Pessoa pessoa : listaPessoas) {
			membrosDto.add(this.converterParaMembrolDto(pessoa));	
		}
		String entity = new ObjectMapper().writeValueAsString(membrosDto);
		
		return ResponseEntity.ok(entity);
	}
	
	@GetMapping(value = "/pastoral/pag/{pastoralId}")
	public ResponseEntity<Response<Page<MembroDTO>>> listarPessoasPorPastoralPaginado(
			@PathVariable("pastoralId") Long pastoralId,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		log.info("Buscando membros por ID do pastoral: {}, página: {}", pastoralId, pag);
		Response<Page<MembroDTO>> response = new Response<Page<MembroDTO>>();

				
		Page<PessoaPastoral> pessoasPastoral = this.pessoaPastoralService
				.listarPorPastoralPaginado(pastoralId, PageRequest.of(pag, this.qtdPorPagina, Sort.by(Direction.valueOf(dir),ord)));
	
		Page<MembroDTO> menbrosDto= (Page<MembroDTO>) pessoasPastoral
				.map(membro -> this.converterParaMembrolDto(membro));

		response.setData(menbrosDto);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna lista de funcoes dos membros da pastoral.
	 * 
	 * @param 
	 * @return ResponseEntity<Response<List<TipoParticipantePastoral>>>
	 */
	@GetMapping(value = "/funcao")
	public ResponseEntity<Response<TipoParticipantePastoral[]>> listarTipoParticipantePastoral() {
		
		Response<TipoParticipantePastoral[]> response = new Response<TipoParticipantePastoral[]>();		
		response.setData(TipoParticipantePastoral.values());
					
		return ResponseEntity.ok(response);
	}
	
	
	private MembroDTO converterParaMembrolDto(PessoaPastoral pessoaPastoral) {
		MembroDTO membroDTO = new MembroDTO();
		membroDTO.setId(Optional.of(pessoaPastoral.getId()));
		membroDTO.setNome(pessoaPastoral.getPessoa().getNome());
		membroDTO.setPessoaId(pessoaPastoral.getPessoa().getId());
		membroDTO.setTipoParticipantePastoral(pessoaPastoral.getTipoParticipantePastoral());
		membroDTO.setPastoralId(pessoaPastoral.getPastoral().getId());
		return membroDTO;
	}
	
	private MembroDTO converterParaMembrolDto(Pessoa pessoa) {
		MembroDTO membroDTO = new MembroDTO();
		membroDTO.setId(Optional.of(pessoa.getId()));
		membroDTO.setNome(pessoa.getNome());
		membroDTO.setPessoaId(pessoa.getId());
		return membroDTO;
	}
	
	/**
	 * Retorna a listagem de pastoral de uma pessoa.
	 * 
	 * @param pessoaID
	 * @return ResponseEntity<Response<PessoaPastoralDTO>>
	 */
	@GetMapping(value = "/pessoa/{pessoaID}")
	public ResponseEntity<Response<List<PessoaPastoralDTO>>> listarPastoraisPorPessoa(
			@PathVariable("pessoaID") Long pessoaID) {
		log.info("Buscando pessoapastoral por ID da pessoa: {}", pessoaID);
		Response<List<PessoaPastoralDTO>> response = new Response<List<PessoaPastoralDTO>>();

		
		List<PessoaPastoral> listaPastoraisPessoa = this.pessoaPastoralService.listarPorPessoa(pessoaID);
		
		response.setData(this.converterParaListaPessoaPastoralDto(listaPastoraisPessoa));
		
		return ResponseEntity.ok(response);
	}
	
	
	

	private void validarDadosParaIncluir(BindingResult result, Optional<Pessoa> pessoa, Optional<Pastoral> pastoral) {
		if (!pessoa.isPresent()) {
			result.addError(new ObjectError("pessoaPastoral", "Pessoa não encontrado."));			
		}else if (!pastoral.isPresent()) {
			result.addError(new ObjectError("pessoaPastoral", "Pastoral não encontrado."));			
		}else if (pessoaPastoralService.buscarPorPessoaAndPastoral(pessoa.get().getId(), pastoral.get().getId()).isPresent()) {
			result.addError(new ObjectError("pessoaPastoral", "Pessoa já é participate da pastoral."));
		}
	}
	
	
	/**
	 * Converte uma entidade pessoaPastoral para seu respectivo DTO.
	 * 
	 * @param pessoaPastoral
	 * @return dto
	 */
	private List<PessoaPastoralDTO> converterParaListaPessoaPastoralDto(List<PessoaPastoral> listaPessoaPastoral) {
		List<PessoaPastoralDTO> listaDto = new ArrayList<PessoaPastoralDTO>();
		for (PessoaPastoral pessoaPastoral : listaPessoaPastoral) {
			PessoaPastoralDTO dto = new PessoaPastoralDTO();
			dto.setId(pessoaPastoral.getId());
			dto.setTipoParticipantePastoral(pessoaPastoral.getTipoParticipantePastoral());
			dto.setPastoral_id(pessoaPastoral.getPastoral().getId());
			dto.setPessoa_id(pessoaPastoral.getPessoa().getId());
			listaDto.add(dto);
		}

		return listaDto;
	}
	
	/**
	 * Converte uma entidade pessoaPastoral para seu respectivo DTO.
	 * 
	 * @param pessoaPastoral
	 * @return dto
	 */
	private PessoaPastoralDTO converterParaPessoaPastoralDto(PessoaPastoral pessoaPastoral) {
		PessoaPastoralDTO dto = new PessoaPastoralDTO();
		dto.setId(pessoaPastoral.getId());
		dto.setTipoParticipantePastoral(pessoaPastoral.getTipoParticipantePastoral());
		dto.setPastoral_id(pessoaPastoral.getPastoral().getId());
		dto.setPessoa_id(pessoaPastoral.getPessoa().getId());

		return dto;
	}
	
	
	
	private PessoaPastoral converterDtoParaPessoaPastoral(PessoaPastoralDTO pessoaPastoralDto, Optional<Pastoral> pastoral, Optional<Pessoa> pessoa) {
		PessoaPastoral pessoaPastoral = new PessoaPastoral();
		pessoaPastoral.setPastoral(pastoral.get());
		pessoaPastoral.setPessoa(pessoa.get());
		pessoaPastoral.setTipoParticipantePastoral(pessoaPastoralDto.getTipoParticipantePastoral());
		pessoaPastoral.setId(pessoaPastoralDto.getId());
		
		return pessoaPastoral;
		
	}
}
