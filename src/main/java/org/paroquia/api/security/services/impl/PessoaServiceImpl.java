package org.paroquia.api.security.services.impl;

import java.util.Optional;

import org.paroquia.api.entities.Pessoa;
import org.paroquia.api.repositories.PessoaRepository;
import org.paroquia.api.security.services.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class PessoaServiceImpl implements PessoaService {
	
	@Autowired
	private PessoaRepository paroquianoRepository;
	
	public Optional<Pessoa> buscarPorEmail(String email) {
		return Optional.ofNullable(this.paroquianoRepository.findByEmail(email));
	}
}