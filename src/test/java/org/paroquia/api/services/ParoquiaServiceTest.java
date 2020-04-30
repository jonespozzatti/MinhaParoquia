package org.paroquia.api.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.paroquia.api.entities.Paroquia;
import org.paroquia.api.repositories.ParoquiaRespository;
import org.paroquia.api.sevices.ParoquiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class ParoquiaServiceTest {

	@MockBean
	private  ParoquiaRespository paroquiaRespository;

	@Autowired
	private ParoquiaService paroquiaService;

	private static final String CNPJ = "51463645000100";

	@BeforeEach
	public void setUp() throws Exception {
		BDDMockito.given(this.paroquiaRespository.findByCnpj(Mockito.anyString())).willReturn(new Paroquia());
		BDDMockito.given(this.paroquiaService.salvar(Mockito.any(Paroquia.class))).willReturn(new Paroquia());
	}

	@Test
	public void testBuscarEmpresaPorCnpj() {
		Optional<Paroquia> paroquia = this.paroquiaService.buscarPorCNPJ(CNPJ);

		assertTrue(paroquia.isPresent());
	}
	
	@Test
	public void testPersistirEmpresa() {
		Paroquia paroquia = this.paroquiaService.salvar(new Paroquia());

		assertNotNull(paroquia);
	}

}