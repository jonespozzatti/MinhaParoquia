package org.paroquia.api.sevices;
import java.util.Optional;

import org.paroquia.api.entities.Pessoa;
import org.paroquia.api.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class PessoaService  {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Optional<Pessoa> buscarPorEmail(String email) {
		return Optional.ofNullable(this.pessoaRepository.findByEmail(email));
	}
	
	public Pessoa salvar(Pessoa pessoa) {
		return this.pessoaRepository.save(pessoa);
	}

	public Optional<Pessoa> buscarPorId(Long id) {
		return this.pessoaRepository.findById(id);
	}
	
	public Optional<Pessoa> buscarPorCPF(String cpf) {
		return Optional.ofNullable(this.pessoaRepository.findByCpf(cpf));
	}
}