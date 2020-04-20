package org.paroquia.api.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.paroquia.api.entities.Paroquia;
import org.paroquia.api.entities.Pastoral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@NamedQueries({
		@NamedQuery(name = "PastoralRepository.findByParoquiaId", 
				query = "SELECT past FROM Pastoral past WHERE past.paroquia.id = :paroquiaId") })
public interface PastoralRepository extends JpaRepository<Pastoral, Long>{
	
	List<Pastoral> findByParoquia(Paroquia paroquia);
	Optional<Pastoral> findById(Long id);
	Pastoral findByEmail(String email);
	List<Pastoral> findByParoquiaId(@Param("paroquiaId") Long paroquiaId);
	
}
