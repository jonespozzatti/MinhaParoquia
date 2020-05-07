package org.paroquia.api.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.paroquia.api.dtos.MatriculaDTO;
import org.paroquia.api.entities.Matricula;
import org.paroquia.api.entities.Pessoa;
import org.paroquia.api.entities.Turma;
import org.paroquia.api.responses.Response;
import org.paroquia.api.sevices.MatriculaService;
import org.paroquia.api.sevices.PessoaService;
import org.paroquia.api.sevices.TurmaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matricula")
public class MatriculaController {
	
private static final Logger log = LoggerFactory.getLogger(PastoralController.class);
	
	@Autowired
	private MatriculaService matriculaService;
	
	@Autowired
	private TurmaService turmaService;
		
	@Autowired
	private PessoaService pessoaService;
	
	/**
	 * Matricular um pessoa em uma turma.
	 * 
	 * @param MatriculaDTO
	 * @return ResponseEntity<Response<MatriculaDTO>>
	 */
	@PostMapping
	public ResponseEntity<Response<MatriculaDTO>> matricular(@Valid @RequestBody MatriculaDTO matriculaDTO,
			BindingResult result) {
		
		log.info("Matricular o paroquiano na turma: {}", matriculaDTO.toString());
		Response<MatriculaDTO> response = new Response<MatriculaDTO>();
		
		Optional<Pessoa> pessoa = this.pessoaService.buscarPorId(matriculaDTO.getPessoaId());
		Optional<Turma> turma = this.turmaService.buscarTurmaPorId(matriculaDTO.getTurmaId());
		
		validarDadosParaIncluir(result, pessoa, turma);
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Matricula matricula = this.converterDtoParaMatricula(matriculaDTO, turma.get(), pessoa.get());
			
		matricula = this.matriculaService.salvar(matricula);
		
		response.setData(this.converterParaMatriculaDto(matricula));
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Validar a entidade ao realizar a  matricula de um aluno.
	 * 
	 * @param BindingResult
	 * @param Optional<Pessoa>
	 * @param Optional<Turma>
	 * @return void
	 */
	private void validarDadosParaIncluir(BindingResult result, Optional<Pessoa> pessoa, Optional<Turma> turma) {
		if (!pessoa.isPresent()) {
			result.addError(new ObjectError("matricula", "Pessoa não encontrada."));			
		}else if (!turma.isPresent()) {
			result.addError(new ObjectError("matricula", "Turma não encontrada."));			
		}else if (matriculaService.buscarPorPessoaAndTurma(pessoa.get().getId(), turma.get().getId()).isPresent()) {
			result.addError(new ObjectError("pessoaPastoral", "Pessoa já é participate da pastoral."));
		}
	}
	
	
	/**
	 * Converte uma entidade pessoaPastoral para seu respectivo DTO.
	 * 
	 * @param pessoaPastoral
	 * @return dto
	 */
	private MatriculaDTO converterParaMatriculaDto(Matricula matricula) {
		MatriculaDTO dto = new MatriculaDTO();
		dto.setId(matricula.getId());
		dto.setObservacao(matricula.getObservacao());
		dto.setPessoaId(matricula.getPessoa().getId());
		dto.setSituacaoMatricula(matricula.getSituacaoMatricula());
		dto.setTipoPessoa(matricula.getTipoPessoa());
		dto.setTurmaId(matricula.getTurma().getId());

		return dto;
	}
	
	
	
	private Matricula converterDtoParaMatricula(MatriculaDTO matriculaDTO, Turma turma, Pessoa pessoa) {
		Matricula matricula = new Matricula();
		matricula.setTurma(turma);
		matricula.setPessoa(pessoa);
		matricula.setObservacao(matriculaDTO.getObservacao());
		matricula.setId(matriculaDTO.getId());
		matricula.setSituacaoMatricula(matriculaDTO.getSituacaoMatricula());
		matricula.setTipoPessoa(matriculaDTO.getTipoPessoa());
		
		return matricula;
		
	}

}
