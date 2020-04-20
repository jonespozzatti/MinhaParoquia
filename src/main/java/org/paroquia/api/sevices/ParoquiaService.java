package org.paroquia.api.sevices;

import java.util.Optional;

import org.paroquia.api.entities.Paroquia;
import org.paroquia.api.repositories.ParoquiaRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParoquiaService {
	
	@Autowired
	private ParoquiaRespository paroquiaRespository;
	
	public Paroquia salvar(Paroquia paroquia) {
		
		return paroquiaRespository.save(paroquia);
	}
	
	public Optional<Paroquia> buscarPorId(Long id) {
				
		return paroquiaRespository.findById(id);
	}

	public Optional<Paroquia> buscarPorCNPJ(String cnpj) {
		return Optional.ofNullable(paroquiaRespository.findByCnpj(cnpj));
	}
	
	public Optional<Paroquia> buscarPorEmail(String email) {
		return Optional.ofNullable(paroquiaRespository.findByEmail(email));
	}
	
}
