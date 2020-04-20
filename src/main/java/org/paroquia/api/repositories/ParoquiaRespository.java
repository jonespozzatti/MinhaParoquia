package org.paroquia.api.repositories;

import java.util.Optional;

import org.paroquia.api.entities.Paroquia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly = true)
public interface ParoquiaRespository  extends JpaRepository<Paroquia, Long>{
	
	Paroquia findByCnpj(String cnpj);
	
	Optional<Paroquia> findById(Long id);
	
	Paroquia findByEmail(String email);
}
