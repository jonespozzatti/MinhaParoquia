package org.paroquia.api.sevices;

import java.util.List;
import java.util.Optional;

import org.paroquia.api.entities.Matricula;
import org.paroquia.api.enums.TipoPessoa;
import org.paroquia.api.repositories.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatriculaService {
	
	@Autowired
	private MatriculaRepository matriculaRepository;
	
	public Optional<Matricula> buscarPorId(Long id) {
		return matriculaRepository.findById(id);
	}
	
	public List<Matricula> listarPorTurma(Long turmaId){
		return matriculaRepository.findByTurmaId(turmaId);
	}
	
	public List<Matricula> listarPorPessoa(Long pessoaId){
		return matriculaRepository.findByPessoaId(pessoaId);
	}
	
	public List<Matricula> listarPorTurmaPorTipoPessoa(TipoPessoa tipoPessoa, Long turmaId){
		return matriculaRepository.findByTipoPessoaAndTurmaId(tipoPessoa, turmaId);
	}
	
	public Matricula salvar(Matricula matricula) {
		return matriculaRepository.save(matricula);
	}
	
	public Optional<Matricula> buscarPorPessoaAndTurma(Long pessoaId, Long turmaId) {
		return matriculaRepository.findByPessoaIdAndTurmaId(pessoaId, turmaId);		
	}
	
	public void remover(Long id) {
		this.matriculaRepository.deleteById(id);
	}

}
