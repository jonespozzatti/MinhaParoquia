package org.paroquia.api.sevices;

import java.util.Optional;

import org.paroquia.api.entities.Paroquia;
import org.paroquia.api.repositories.ParoquiaRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ParoquiaService {
	
	private static final Logger log = LoggerFactory.getLogger(ParoquiaService.class);
	
	@Autowired
	private ParoquiaRespository paroquiaRespository;
	
	public Paroquia salvar(Paroquia paroquia) {
		
		return paroquiaRespository.save(paroquia);
	}
	
	@Cacheable("buscarParoquiaPorId")
	public Optional<Paroquia> buscarParoquiaPorId(Long id) {
		log.info("Buscando a paroquia pelo ID {}", id);
		return paroquiaRespository.findById(id);
	}

	
	public Optional<Paroquia> buscarPorCNPJ(String cnpj) {
		log.info("Buscando a paroquia pelo CNPJ {}", cnpj);
		return Optional.ofNullable(paroquiaRespository.findByCnpj(cnpj));
	}
	
	public Optional<Paroquia> buscarPorEmail(String email) {
		log.info("Buscando a paroquia pelo email {}", email);
		return Optional.ofNullable(paroquiaRespository.findByEmail(email));
	}
	
}
