package org.paroquia.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.paroquia.api.entities.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@NamedQueries({
	@NamedQuery(name = "TurmaRepository.findByCursoId", 
			query = "SELECT turma FROM Turma turma WHERE turma.curso.id = :cursoId") })
public interface TurmaRepository extends JpaRepository<Turma, Long> {
	List<Turma> findByCursoId(@Param("cursoId") Long cursoId);

}
