package org.paroquia.api.security.services;

import java.util.Optional;

import org.paroquia.api.entities.Pessoa;


public interface PessoaService {

	/**
	 * Busca e retorna um usuário dado um email.
	 * 
	 * @param email
	 * @return Optional<Usuario>
	 */
	Optional<Pessoa> buscarPorEmail(String email);

}