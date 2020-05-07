package org.paroquia.api.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.paroquia.api.entities.Matricula;
import org.paroquia.api.enums.TipoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@NamedQueries({
	@NamedQuery(name = "MatriculaRepository.findByTurmaId", 
			query = "SELECT matricula FROM Matricula matricula WHERE matricula.turma.id = :turmaId"),	
	@NamedQuery(name = "MatriculaRepository.findByPessoaId", 
			query = "SELECT matricula FROM Matricula matricula WHERE matricula.pessoa.id = :pessoaId"),
	@NamedQuery(name="MatriculaRepository.findByPessoaIdAndTurmaId",
			query = "SELECT matricula FROM Matricula matricula WHERE matricula.turma.id = :turmaId AND matricula.pessoa.id = :pessoaId"),
	@NamedQuery(name="MatriculaRepository.findByTipoPessoaAndTurmaId",
	query = "SELECT matricula FROM Matricula matricula WHERE matricula.turma.id = :turmaId AND matricula.tipoPessoa = :tipoPessoa")
	})
public interface MatriculaRepository extends JpaRepository<Matricula, Long>{
	
	List<Matricula> findByTurmaId(@Param("turmaId") Long turmaId);
	
	List<Matricula> findByPessoaId(@Param("pessoaId") Long pessoId);
	
	List<Matricula> findByTipoPessoaAndTurmaId(@Param("tipoPessoa") TipoPessoa tipoPessoa, @Param("turmaId") Long turmaId);
	
	Optional<Matricula> findByPessoaIdAndTurmaId(@Param("pessoaId") Long pessoaId, @Param("turmaId") Long turmaId);

}
