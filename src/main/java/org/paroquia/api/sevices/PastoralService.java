package org.paroquia.api.sevices;

import java.util.List;
import java.util.Optional;

import org.paroquia.api.entities.Pastoral;
import org.paroquia.api.repositories.PastoralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PastoralService {

	@Autowired
	private PastoralRepository pastoralRepository;
	
	public Pastoral salvar(Pastoral pastoral) {
		return pastoralRepository.save(pastoral);
	}
	
	public Optional<Pastoral> buscarPorId(Long id) {
		return pastoralRepository.findById(id);
	}
	
	public Optional<Pastoral> buscarPorEmail(String email) {
		return Optional.ofNullable(pastoralRepository.findByEmail(email));
	}
	
	public List<Pastoral> listarPorParoquia(Long paroquiaId){
		return pastoralRepository.findByParoquiaId(paroquiaId);
	}
}
