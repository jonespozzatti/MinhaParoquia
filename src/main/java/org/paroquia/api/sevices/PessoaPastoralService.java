package org.paroquia.api.sevices;

import java.util.List;
import java.util.Optional;

import org.paroquia.api.entities.PessoaPastoral;
import org.paroquia.api.repositories.PessoaPastoralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaPastoralService {

	@Autowired
	private PessoaPastoralRepository pessoaPastoralRepository;
	
	
	public Optional<PessoaPastoral> buscarPorId(Long id) {
		return pessoaPastoralRepository.findById(id);
	}
	
	public List<PessoaPastoral> listarPorPastoral(Long pastoralId){
		return pessoaPastoralRepository.findByPastoralId(pastoralId);
	}
	
	public List<PessoaPastoral> listarPorPessoa(Long pessoaId){
		return pessoaPastoralRepository.findByPessoaId(pessoaId);
	}
	
	public PessoaPastoral salvar(PessoaPastoral pessoaPastoral) {
		return pessoaPastoralRepository.save(pessoaPastoral);
	}
	
	public Optional<PessoaPastoral> buscarPorPessoaAndPastoral(Long pessoaId, Long pastoralId) {
		return pessoaPastoralRepository.findByPessoaIdAndPastoralId(pessoaId, pastoralId);		
	}
	
	public void remover(Long id) {
		this.pessoaPastoralRepository.deleteById(id);
	}
}
