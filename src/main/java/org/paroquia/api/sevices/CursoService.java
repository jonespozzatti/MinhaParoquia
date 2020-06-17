package org.paroquia.api.sevices;

import java.util.List;
import java.util.Optional;

import org.paroquia.api.entities.Curso;
import org.paroquia.api.repositories.CursoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CursoService {

	private static final Logger log = LoggerFactory.getLogger(CursoService.class);
	
	@Autowired
	private CursoRepository cursoRepository;
	
	public Curso salvar(Curso curso) {
		return cursoRepository.save(curso);
	}
	
	public Optional<Curso> buscarCursoPorId(Long id) {
		log.info("Buscando curso por ID ", id);
		return cursoRepository.findById(id);
	}
	
	public List<Curso> listarCursoPorParoquia(Long paroquiaId){
		log.info("Listando cursos da paroquia ID ", paroquiaId);
		return cursoRepository.findByParoquiaId(paroquiaId);
	}
	
	public Optional<Curso> buscarPorNome(String nome) {
		log.info("Buscando curso pelo nome ", nome);
		return Optional.ofNullable(cursoRepository.findByNome(nome));
	}
	
	public Page<Curso> listarCursoPorParoquiaPaginado(Long paroquiaID, PageRequest pageRequest) {
		log.info("Listando cursos paginado pela paroquiaID ", paroquiaID);
		return this.cursoRepository.findByParoquiaId(paroquiaID, pageRequest);
	}
	
	public void remover(Long id) {
		cursoRepository.deleteById(id);
	}
}
