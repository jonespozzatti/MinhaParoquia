package org.paroquia.api.repositories;

import java.util.List;

import org.paroquia.api.entities.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
	
	Pessoa findByEmail(String email);
	Pessoa findByCpf(String cpf);
	@Query(value = "SELECT pes FROM Pessoa pes WHERE pes.id NOT IN (SELECT pespas.pessoa.id FROM PessoaPastoral pespas WHERE  "
            + "pespas.pastoral.id = :pastoralId GROUP By pespas.pessoa.id) ")
	List<Pessoa> listPessoasNaoPertencemPastoral(@Param("pastoralId") Long pastoralId);

}
