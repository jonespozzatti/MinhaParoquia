package org.paroquia.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.paroquia.api.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@NamedQueries({
	@NamedQuery(name = "CursoRepository.findByParoquiaId", 
			query = "SELECT curso FROM Curso curso WHERE curso.paroquia.id = :paroquiaId") })
public interface CursoRepository extends JpaRepository<Curso, Long>{
	
	List<Curso> findByParoquiaId(@Param("paroquiaId") Long paroquiaId);
	Curso findByNome(String nome);

}
