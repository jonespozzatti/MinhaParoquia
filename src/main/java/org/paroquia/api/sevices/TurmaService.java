package org.paroquia.api.sevices;

import java.util.List;
import java.util.Optional;

import org.paroquia.api.entities.Turma;
import org.paroquia.api.repositories.TurmaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurmaService {
	
	private static final Logger log = LoggerFactory.getLogger(TurmaService.class);
	
	@Autowired
	private TurmaRepository turmaRepository;
	
	public Turma salvar(Turma turma) {
		return turmaRepository.save(turma);
	}
	
	public Optional<Turma> buscarTurmaPorId(Long id) {
		log.info("Buscando turma por ID ", id);
		return turmaRepository.findById(id);
	}
	
	public List<Turma> listarTurmaPorCurso(Long cursoId) {
		log.info("Listando turmas do curso ID ", cursoId);
		return turmaRepository.findByCursoId(cursoId);
	}

}
