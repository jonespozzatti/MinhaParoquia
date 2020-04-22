package org.paroquia.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.paroquia.api.dtos.PastoralDTO;
import org.paroquia.api.dtos.PessoaPastoralDTO;
import org.paroquia.api.entities.Paroquia;
import org.paroquia.api.entities.Pastoral;
import org.paroquia.api.entities.Pessoa;
import org.paroquia.api.entities.PessoaPastoral;
import org.paroquia.api.enums.TipoParticipantePastoral;
import org.paroquia.api.responses.Response;
import org.paroquia.api.sevices.ParoquiaService;
import org.paroquia.api.sevices.PastoralService;
import org.paroquia.api.sevices.PessoaPastoralService;
import org.paroquia.api.sevices.PessoaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pastoral")
public class PastoralController {

	private static final Logger log = LoggerFactory.getLogger(PastoralController.class);
	
	@Autowired
	private PastoralService pastoralService;
	
	@Autowired
	private ParoquiaService paroquiaService;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private PessoaPastoralService pessoaPastoralService;
	

	public PastoralController() {
		
	}
	
	/**
	 * Retorna a listagem de pastorais de um paróquia.
	 * 
	 * @param paroquiaID
	 * @return ResponseEntity<Response<ParoquiaDTO>>
	 */
	@GetMapping(value = "/paroquia/{paroquiaId}")
	public ResponseEntity<Response<List<PastoralDTO>>> listarPorParoquiaId(
			@PathVariable("paroquiaId") Long paroquiaId) {
		log.info("Buscando pastoral por ID da paróquia: {}", paroquiaId);
		Response<List<PastoralDTO>> response = new Response<List<PastoralDTO>>();

		
		List<Pastoral> pastorais = this.pastoralService.listarPorParoquia(paroquiaId);
		
		if (pastorais.isEmpty()) {
			log.info("Nenhuma pastoral encontrada para a paróquia de id: {}", paroquiaId);
			response.getErrors().add("Nenhuma pastoral encontrada para a paróquia de id " + paroquiaId);
			return ResponseEntity.badRequest().body(response);
		}
		
		List<PastoralDTO> pastoraisDto = new ArrayList<PastoralDTO>(0);
		for (Pastoral pastoral : pastorais) {
			pastoraisDto.add(this.converterParaPastoralDto(pastoral));			
		}
		
		response.setData(pastoraisDto);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna uma pastoral pelo id.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<PastoralDTO>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<PastoralDTO>> obter(
			@PathVariable("id") Long id) {
		log.info("Buscando pastoral por ID: {}", id);
		Response<PastoralDTO> response = new Response<PastoralDTO>();
		
		Optional<Pastoral> pastoral = this.pastoralService.buscarPorId(id);
		
		if (!pastoral.isPresent()) {
			log.info("Pastoral não encontrada para o ID: {}", id);
			response.getErrors().add("Pastoral não encontrada para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		
		response.setData(this.converterParaPastoralDto(pastoral.get()));
					
		return ResponseEntity.ok(response);
	}
	/**
	 * Atualiza os dados de uma pastoral.
	 * 
	 * @param id
	 * @param pastoralDto
	 * @param result
	 * @return ResponseEntity<Response<PastoralDTO>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<PastoralDTO>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody PastoralDTO pastoralDTO, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando Pastoral: {}", pastoralDTO.toString());
		Response<PastoralDTO> response = new Response<PastoralDTO>();

		Optional<Pastoral> pastoral = this.pastoralService.buscarPorId(id);
		if (!pastoral.isPresent()) {
			result.addError(new ObjectError("pastoral", "Pastoral não encontrada."));
		}

		this.atualizarDadosPastoral(pastoral.get(), pastoralDTO, result);

		if (result.hasErrors()) {
			log.error("Erro validando paróquia: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.pastoralService.salvar(pastoral.get());
		response.setData(this.converterParaPastoralDto(pastoral.get()));

		return ResponseEntity.ok(response);
	}
	
	/**
	 * Cadastra uma nova pastoral de um paróquia.
	 * 
	 * @param pastoralDTO
	 * @return ResponseEntity<Response<PessoaPastoralDTO>>
	 */
	@PostMapping
	public ResponseEntity<Response<PessoaPastoralDTO>> cadastrar(@Valid @RequestBody PastoralDTO pastoralDTO,
			BindingResult result) {
		
		log.info("Cadastrando Pastoral: {}", pastoralDTO.toString());
		Response<PessoaPastoralDTO> response = new Response<PessoaPastoralDTO>();
		
		validarDadosExistentes(pastoralDTO, result);
		Pastoral pastoral = this.converterDtoParaPastoral(pastoralDTO, result);
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		Optional<Pessoa> pessoaCoordenador = null;
		if (pastoralDTO.getCoordenadorPastoralId() != null) {
			pessoaCoordenador = this.pessoaService.buscarPorId(pastoralDTO.getCoordenadorPastoralId());
			if (!pessoaCoordenador.isPresent()) {
				result.addError(new ObjectError("pastoral", "Coordenador não encontrado."));
				
			} 
		}
		PessoaPastoral pessoaPastoral = popularPessoaPastoral(pastoral, pessoaCoordenador);
		pessoaPastoral = this.pessoaPastoralService.salvar(pessoaPastoral);
		
		response.setData(this.converterParaPessoaPastoralDto(pessoaPastoral));
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Atualiza os dados da pastoral com base nos dados encontrados no DTO.
	 * 
	 * @param pastoral
	 * @param pastoralDTO
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosPastoral(Pastoral pastoral, PastoralDTO pastoralDTO, BindingResult result)
			throws NoSuchAlgorithmException {
		pastoral.setDescricao(pastoralDTO.getDescricao());

		if (!pastoral.getEmail().equals(pastoral.getEmail())) {
			this.pastoralService.buscarPorEmail(pastoralDTO.getEmail())
					.ifPresent(func -> result.addError(new ObjectError("email", "Email já existente.")));
			pastoral.setEmail(pastoralDTO.getEmail());
		}
		
		pastoral.setNome(pastoral.getNome());

	}
	
	/**
	 * Converte uma entidade pastoral para seu respectivo DTO.
	 * 
	 * @param pastoral
	 * @return dto
	 */
	private PastoralDTO converterParaPastoralDto(Pastoral pastoral) {
		PastoralDTO dto = new PastoralDTO();
		dto.setId(pastoral.getId());
		dto.setDescricao(pastoral.getDescricao());
		dto.setEmail(pastoral.getEmail());
		dto.setNome(pastoral.getNome());
		dto.setParoquiaId(pastoral.getParoquia().getId());
		

		return dto;
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
	
	
	/**
	 * Verifica se a pastoral está cadastrada.
	 * 
	 * @param pastoralDTO
	 * @param result
	 */
	private void validarDadosExistentes(PastoralDTO pastoralDTO, BindingResult result) {
		
		this.pastoralService.buscarPorEmail(pastoralDTO.getEmail())
		.ifPresent(func -> result.addError(new ObjectError("Pastoral", "Email já existente.")));
		
	}

	/**
	 * Converte os dados do DTO para Pastoral.
	 * 
	 * @param pastoralDTO
	 * @param result
	 * @return Pastoral
	 * @throws NoSuchAlgorithmException
	 */
	private Pastoral converterDtoParaPastoral(PastoralDTO pastoralDTO, BindingResult result) {
		Pastoral pastoral = new Pastoral();
		pastoral.setDescricao(pastoralDTO.getDescricao());
		pastoral.setNome(pastoralDTO.getNome());
		pastoral.setId(pastoralDTO.getId());

		if (pastoralDTO.getParoquiaId() != null) {
			Optional<Paroquia> paroquia = this.paroquiaService.buscarPorId(pastoralDTO.getParoquiaId());
			if (paroquia.isPresent()) {
				pastoral.setParoquia(paroquia.get());
			} else {
				result.addError(new ObjectError("pastoral", "Paróquia não encontrado."));
			}
		}
		pastoral.setEmail(pastoralDTO.getEmail());
		
		return pastoral;
	}

	private PessoaPastoral popularPessoaPastoral(Pastoral pastoral, Optional<Pessoa> pessoaCoordenador) {
		PessoaPastoral pessoaPastoral = new PessoaPastoral();
		pessoaPastoral.setPessoa(pessoaCoordenador.get());
		pessoaPastoral.setPastoral(pastoral);
		pessoaPastoral.setTipoParticipantePastoral(TipoParticipantePastoral.COORDENADOR);
		
		return pessoaPastoral;
		
	}
	
}
