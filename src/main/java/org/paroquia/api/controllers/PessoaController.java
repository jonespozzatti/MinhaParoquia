package org.paroquia.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Optional;

import javax.validation.Valid;

import org.paroquia.api.dtos.PessoaDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/pessoa")
public class PessoaController {
	
	private static final Logger log = LoggerFactory.getLogger(PessoaController.class);
	
	@Autowired
	private PastoralService pastoralService;
		
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private PessoaPastoralService pessoaPastoralService;
	
	@Value("${pastoral.id_default_paroquiano}")
	private Long pastoral_id_default;
	
	@GetMapping(value = "/{nome}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String exemplo(@PathVariable("nome") String nome) {
		return "Olá " + nome;
	}
	
	/**
	 * Cadastra um paroquiano de um paróquia.
	 * 
	 * @param pessoaDTO
	 * @return ResponseEntity<Response<PessoaDTO>>
	 */
	@PostMapping
	public ResponseEntity<Response<PessoaDTO>> cadastrar(@Valid @RequestBody PessoaDTO pessoaDTO,
			BindingResult result) {
		
		log.info("Cadastrando Pessoa: {}", pessoaDTO.toString());
		Response<PessoaDTO> response = new Response<PessoaDTO>();

		Pessoa pessoa = this.converterDtoParaPessoa(pessoaDTO, result);
		validarDadosExistentes(pessoaDTO, result);
		Optional<Pastoral> pastoralDefault = pastoralService.buscarPorId(pastoral_id_default);
		
		if (!pastoralDefault.isPresent()) {
			result.addError(new ObjectError("pessoa", "Pastoral default não encontrada."));
		} 	
				
		if(result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		PessoaPastoral pessoaPastoral = popularPessoaPastoral(pessoa, pastoralDefault);		
		
		pessoaPastoral = this.pessoaPastoralService.salvar(pessoaPastoral);
		
		response.setData(this.converterParaPessoaDto(pessoaPastoral.getPessoa()));
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Atualiza os dados de uma pessoa.
	 * 
	 * @param id
	 * @param pessoaDto
	 * @param result
	 * @return ResponseEntity<Response<PessoaDTO>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<PessoaDTO>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody PessoaDTO pessoaDTO, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando Pessoa: {}", pessoaDTO.toString());
		Response<PessoaDTO> response = new Response<PessoaDTO>();

		Optional<Pessoa> pessoa = this.pessoaService.buscarPorId(id);
		if (!pessoa.isPresent()) {
			result.addError(new ObjectError("pessoa", "Pessoa não encontrada."));
		}

		this.atualizarDadosPessoa(pessoa.get(), pessoaDTO, result);

		if (result.hasErrors()) {
			log.error("Erro validando paróquia: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.pessoaService.salvar(pessoa.get());
		response.setData(this.converterParaPessoaDto(pessoa.get()));

		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna uma pessoa pelo id.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<PessoaDTO>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<PessoaDTO>> obter(
			@PathVariable("id") Long id) {
		log.info("Buscando pessoa por ID: {}", id);
		Response<PessoaDTO> response = new Response<PessoaDTO>();
		
		Optional<Pessoa> pessoa = this.pessoaService.buscarPorId(id);
		
		if (!pessoa.isPresent()) {
			log.info("Pessoa não encontrada para o ID: {}", id);
			response.getErrors().add("Pessoa não encontrada para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		
		response.setData(this.converterParaPessoaDto(pessoa.get()));
					
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Atualiza os dados da pessoa com base nos dados encontrados no DTO.
	 * 
	 * @param pessoa
	 * @param pessoaDTO
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosPessoa(Pessoa pessoa, PessoaDTO pessoaDTO, BindingResult result)
			throws NoSuchAlgorithmException {
		
		pessoa.setCpf(pessoaDTO.getCpf());
		
		try {
			pessoa.setDataNasc(pessoaDTO.getDataNascimetntoConvertida("yyyy-MM-dd"));
		} catch (ParseException e) {
			result.addError(new ObjectError("pessoa", "Data Inválida."));
		}
		
		pessoa.setEmail(pessoaDTO.getEmail());
		pessoa.setEndereco(pessoaDTO.getEndereco());
		pessoa.setId(pessoaDTO.getId());
		pessoa.setNome(pessoaDTO.getNome());
		pessoa.setPerfil(pessoaDTO.getPerfil());
		pessoa.setResponsavel(pessoaDTO.getResponsavel_id() != null ? 
				pessoaService.buscarPorId(pessoaDTO.getResponsavel_id()).get() 
				: null);
		pessoa.setSenha(pessoaDTO.getSenha());
		pessoa.setSexo(pessoaDTO.getSexo());
		pessoa.setTelefoneCelular(pessoaDTO.getTelefoneCelular());
		pessoa.setTelefoneFixo(pessoaDTO.getTelefoneFixo());

	}
	
	/**
	 * Verifica se o paroquiano não existe na base de dados.
	 * 
	 * @param pessoaDto
	 * @param result
	 */
	private void validarDadosExistentes(PessoaDTO pessoaDto, BindingResult result) {
				
		this.pessoaService.buscarPorCPF(pessoaDto.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("pessoa", "CPF já existente.")));

		this.pessoaService.buscarPorEmail(pessoaDto.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("pessoa", "Email já existente.")));
	}
	
		
	/**
	 * Converte uma entidade pessoaPastoral para uma PessoaDTO.
	 * 
	 * @param pessoaPastoral
	 * @return pessoaDto
	 */
	private PessoaDTO converterParaPessoaDto(Pessoa pessoa) {
		PessoaDTO pessoaDto = new PessoaDTO();
		pessoaDto.setId(pessoa.getId());
		pessoaDto.setCpf(pessoa.getCpf());
		pessoaDto.setDataNascString(pessoa.getDataNasc(), "yyyy-MM-dd");
		pessoaDto.setEmail(pessoa.getEmail());
		pessoaDto.setEndereco(pessoa.getEndereco());
		pessoaDto.setNome(pessoa.getNome());
		pessoaDto.setPerfil(pessoa.getPerfil());
		pessoaDto.setResponsavel_id(pessoa.getResponsavel() != null
				? pessoa.getResponsavel().getId()
				: null);
		pessoaDto.setSenha(pessoa.getSenha());
		pessoaDto.setSexo(pessoa.getSexo());
		pessoaDto.setTelefoneCelular(pessoa.getTelefoneCelular());
		pessoaDto.setTelefoneFixo(pessoa.getTelefoneFixo());
		
		return pessoaDto;
	}
	
	
	
	/**
	 * Converte os dados do DTO para Pessoa.
	 * 
	 * @param pessoalDTO
	 * @param result
	 * @return Pessoa
	 * @throws NoSuchAlgorithmException
	 */
	private Pessoa converterDtoParaPessoa(PessoaDTO pessoalDTO, BindingResult result) {
		Pessoa pessoa = new Pessoa();
		
		pessoa.setCpf(pessoalDTO.getCpf());
		
		try {
			pessoa.setDataNasc(pessoalDTO.getDataNascimetntoConvertida("yyyy-MM-dd"));
		} catch (ParseException e) {
			result.addError(new ObjectError("pessoa", "Data Inválida."));
		}
		
		pessoa.setEmail(pessoalDTO.getEmail());
		pessoa.setEndereco(pessoalDTO.getEndereco());
		pessoa.setId(pessoalDTO.getId());
		pessoa.setNome(pessoalDTO.getNome());
		pessoa.setPerfil(pessoalDTO.getPerfil());
		pessoa.setResponsavel(pessoalDTO.getResponsavel_id() != null ? 
				pessoaService.buscarPorId(pessoalDTO.getResponsavel_id()).get() 
				: null);
		pessoa.setSenha(pessoalDTO.getSenha());
		pessoa.setSexo(pessoalDTO.getSexo());
		pessoa.setTelefoneCelular(pessoalDTO.getTelefoneCelular());
		pessoa.setTelefoneFixo(pessoalDTO.getTelefoneFixo());
		
		
		return pessoa;
	}

	private PessoaPastoral popularPessoaPastoral(Pessoa pessoa, Optional<Pastoral> pastoralDefault) {
		PessoaPastoral pessoaPastoral = new PessoaPastoral();
		pessoaPastoral.setPessoa(pessoa);
		pessoaPastoral.setPastoral(pastoralDefault.get());
		pessoaPastoral.setTipoParticipantePastoral(TipoParticipantePastoral.INTEGRANTE);
		
		return pessoaPastoral;
		
	}

}
