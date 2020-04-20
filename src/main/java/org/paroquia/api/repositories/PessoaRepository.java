package org.paroquia.api.repositories;

import org.paroquia.api.entities.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
	
	Pessoa findByEmail(String email);
	Pessoa findByCpf(String cpf);
	
}
