package org.paroquia.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.paroquia.api.dtos.PastoralDTO;
import org.paroquia.api.entities.Paroquia;
import org.paroquia.api.entities.Pastoral;
import org.paroquia.api.responses.Response;
import org.paroquia.api.sevices.ParoquiaService;
import org.paroquia.api.sevices.PastoralService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
		List<PastoralDTO> pastoraisDto = new ArrayList<PastoralDTO>(0);
		for (Pastoral pastoral : pastorais) {
			pastoraisDto.add(this.converterParaPastoralDto(pastoral));			
		}
		
		response.setData(pastoraisDto);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Cadastra uma nova pastoral de um paróquia.
	 * 
	 * @param paroquiaID
	 * @return ResponseEntity<Response<ParoquiaDTO>>
	 */
	@PostMapping
	public ResponseEntity<Response<PastoralDTO>> cadastrar(@Valid @RequestBody PastoralDTO pastoralDTO,
			BindingResult result) {
		
		log.info("Cadastrando Pastoral: {}", pastoralDTO.toString());
		Response<PastoralDTO> response = new Response<PastoralDTO>();
		
		validarDadosExistentes(pastoralDTO, result);
		Pastoral pastoral = this.converterDtoParaPastoral(pastoralDTO, result);
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Pastoral pastoralSalva = this.pastoralService.salvar(pastoral);
		
		response.setData(this.converterParaPastoralDto(pastoralSalva));
		
		return ResponseEntity.ok(response);
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
	
}
