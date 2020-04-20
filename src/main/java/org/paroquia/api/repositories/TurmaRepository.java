package org.paroquia.api.repositories;

import org.paroquia.api.entities.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TurmaRepository extends JpaRepository<Turma, Long> {

}
