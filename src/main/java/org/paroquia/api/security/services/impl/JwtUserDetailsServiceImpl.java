package org.paroquia.api.security.services.impl;

import java.util.Optional;

import org.paroquia.api.entities.Pessoa;
import org.paroquia.api.security.JwtUserFactory;
import org.paroquia.api.sevices.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private PessoaService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Pessoa> funcionario = usuarioService.buscarPorEmail(username);

		if (funcionario.isPresent()) {
			return JwtUserFactory.create(funcionario.get());
		}

		throw new UsernameNotFoundException("Email não encontrado.");
	}

}