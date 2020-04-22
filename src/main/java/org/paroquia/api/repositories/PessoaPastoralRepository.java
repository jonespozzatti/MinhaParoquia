package org.paroquia.api.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.paroquia.api.entities.PessoaPastoral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@NamedQueries({
	@NamedQuery(name = "PessoaPastoralRepository.findByPastoralId", 
			query = "SELECT pespas FROM PessoaPastoral pespas WHERE pespas.pastoral.id = :pastoralId"),	
	@NamedQuery(name="PessoaPastoralRepository.findByPessoaIdAndPastoralId",
			query = "SELECT pespas FROM PessoaPastoral pespas WHERE pespas.pastoral.id = :pastoralId AND pespas.pessoa.id = :pessoaId") 
	})
public interface PessoaPastoralRepository extends JpaRepository<PessoaPastoral, Long>{
	
	Optional<PessoaPastoral> findById(Long id);
	
	List<PessoaPastoral> findByPastoralId(@Param("pastoralId") Long pastoralId);
	
	Optional<PessoaPastoral> findByPessoaIdAndPastoralId(@Param("pessoaId") Long pessoaId, @Param("pastoralId") Long pastoralId);
	

}
