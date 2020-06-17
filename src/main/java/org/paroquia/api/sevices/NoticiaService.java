package org.paroquia.api.sevices;

import java.util.List;
import java.util.Optional;

import org.paroquia.api.entities.Noticia;
import org.paroquia.api.repositories.NoticiaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class NoticiaService {

	private static final Logger log = LoggerFactory.getLogger(NoticiaService.class);
	
	@Autowired
	private NoticiaRepository noticiaRepository;
	
	public Noticia salvar(Noticia noticia) {
		return noticiaRepository.save(noticia);
	}
	
	public Optional<Noticia> buscarNoticiaPorId(Long id) {
		log.info("Buscando noticia por ID ", id);
		return noticiaRepository.findById(id);
	}
	
	public List<Noticia> listarNoticiaPorParoquia(Long paroquiaId){
		log.info("Listando noticias da paroquia ID ", paroquiaId);
		return noticiaRepository.findByParoquiaId(paroquiaId);
	}
	
	public Page<Noticia> listarNoticiaPorParoquiaPaginado(Long paroquiaID, PageRequest pageRequest) {
		log.info("Listando noticias paginado pela paroquiaID ", paroquiaID);
		return this.noticiaRepository.findByParoquiaId(paroquiaID, pageRequest);
	}
	
	public void remover(Long id) {
		noticiaRepository.deleteById(id);
	}
}
