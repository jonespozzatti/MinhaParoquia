package org.paroquia.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.paroquia.api.entities.Noticia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@NamedQueries({
	@NamedQuery(name = "NoticiaRepository.findByParoquiaId", 
			query = "SELECT noticia FROM Noticia noticia WHERE noticia.paroquia.id = :paroquiaId") })
public interface NoticiaRepository extends JpaRepository<Noticia, Long>{
	
	List<Noticia> findByParoquiaId(@Param("paroquiaId") Long paroquiaId);
	Page<Noticia> findByParoquiaId(@Param("paroquiaId") Long paroquiaId, Pageable pageable);

}
