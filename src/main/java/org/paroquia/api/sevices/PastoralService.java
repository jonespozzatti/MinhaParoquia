package org.paroquia.api.sevices;

import java.util.List;
import java.util.Optional;

import org.paroquia.api.entities.Pastoral;
import org.paroquia.api.repositories.PastoralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PastoralService {

	@Autowired
	private PastoralRepository pastoralRepository;
	
	public Page<Pastoral> listarPastoralPorParoquiaPaginado(Long pastoralId, PageRequest pageRequest) {
		return this.pastoralRepository.findByParoquiaId(pastoralId, pageRequest);
	}
	
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
	
	public void remover(Long id) {
		pastoralRepository.deleteById(id);
	}
}
