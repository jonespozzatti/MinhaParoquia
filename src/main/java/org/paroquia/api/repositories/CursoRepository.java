package org.paroquia.api.repositories;

import org.paroquia.api.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CursoRepository extends JpaRepository<Curso, Long>{

}