package org.paroquia.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.paroquia.api.dtos.ParoquiaDTO;
import org.paroquia.api.entities.Paroquia;
import org.paroquia.api.responses.Response;
import org.paroquia.api.sevices.ParoquiaService;
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
@RequestMapping("/api/paroquia")
public class ParoquiaController {
	
	private static final Logger log = LoggerFactory.getLogger(ParoquiaController.class);
	
	@Autowired
	private ParoquiaService paroquiaService;
	
	
	@PostMapping
	public ResponseEntity<Response<ParoquiaDTO>> cadastrar(@Valid @RequestBody ParoquiaDTO paroquiaDTO,
			BindingResult result) {
		
		log.info("Cadastrando Paroquia: {}", paroquiaDTO.toString());
		Response<ParoquiaDTO> response = new Response<ParoquiaDTO>();
		
		validarDadosExistentes(paroquiaDTO, result);
		Paroquia paroquia = this.converterDtoParaParoquia(paroquiaDTO, result);
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Paroquia paroquiaSalva = this.paroquiaService.salvar(paroquia);
		
		response.setData(this.converterParaParoquiaDto(paroquiaSalva));
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<ParoquiaDTO>> obter(@PathVariable("id") Long id) {
		
		log.info("Buscando paroquia por ID: {}", id);
		Response<ParoquiaDTO> response = new Response<ParoquiaDTO>();
		Optional<Paroquia> paroquia = paroquiaService.buscarPorId(id);
		
		if (!paroquia.isPresent()) {
			log.info("Paroquia não encontrada para o ID: {}", id);
			response.getErrors().add("Paroquia não encontrada para o ID " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.converterParaParoquiaDto(paroquia.get()));
		
		return ResponseEntity.ok(response);
	}

	
	
	/**
	 * Popula um DTO com os dados de uma paroquia.
	 * 
	 * @param paroquia
	 * @return ParoquiaDTO
	 */
	private ParoquiaDTO converterParaParoquiaDto(Paroquia paroquia) {
		ParoquiaDTO dto = new ParoquiaDTO();
		dto.setCnpj(paroquia.getCnpj());
		dto.setEndereco(paroquia.getEndereco());
		dto.setLocalizacao(paroquia.getLocalizacao());
		dto.setRazaoSocial(paroquia.getRazaoSocial());
		dto.setId(paroquia.getId());
		dto.setEmail(paroquia.getEmail());
		
		return dto;
	}
	
	
	/**
	 * Verifica se a paroquia está cadastrada.
	 * 
	 * @param paroquiaDTO
	 * @param result
	 */
	private void validarDadosExistentes(ParoquiaDTO paroquiaDTO, BindingResult result) {
		Optional<Paroquia> paroquia = this.paroquiaService.buscarPorCNPJ(paroquiaDTO.getCnpj());
		if (paroquia.isPresent()) {
			result.addError(new ObjectError("paroquia", "Paroquia já cadastrada com o CNPJ "+ paroquiaDTO.getCnpj() +".")   );
		}
		this.paroquiaService.buscarPorEmail(paroquiaDTO.getEmail())
		.ifPresent(func -> result.addError(new ObjectError("paroquia", "Email já existente.")));
		
	}

	/**
	 * Converte os dados do DTO para paroquia.
	 * 
	 * @param paroquiaDTO
	 * @param result
	 * @return Paroquia
	 * @throws NoSuchAlgorithmException
	 */
	private Paroquia converterDtoParaParoquia(ParoquiaDTO paroquiaDTO, BindingResult result) {
		Paroquia paroquia = new Paroquia();
		paroquia.setCnpj(paroquiaDTO.getCnpj());
		paroquia.setEndereco(paroquiaDTO.getEndereco());
		paroquia.setId(paroquiaDTO.getId());
		paroquia.setLocalizacao(paroquiaDTO.getLocalizacao());
		paroquia.setRazaoSocial(paroquiaDTO.getRazaoSocial());
		paroquia.setEmail(paroquiaDTO.getEmail());
		
		return paroquia;
	}

}