package org.paroquia.api.repositories;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.paroquia.api.entities.Paroquia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class ParoquiaRepositoryTest {
	
	@Autowired
	private ParoquiaRespository paroquiaRespository;
	
	private static final String CNPJ = "51463645000100";

	@BeforeEach
	public void setUp() throws Exception {
		Paroquia paroquia = new Paroquia();
		paroquia.setRazaoSocial("Paroquia de exemplo");
		paroquia.setCnpj(CNPJ);
		paroquia.setEmail("teste@gmail.com");
		this.paroquiaRespository.save(paroquia);
	}
	
	@AfterEach
    public final void tearDown() { 
		this.paroquiaRespository.deleteAll();
	}

	@Test
	public void testBuscarPorCnpj() {
		Paroquia paroquia = this.paroquiaRespository.findByCnpj(CNPJ);
		assertEquals(CNPJ, paroquia.getCnpj());
	}

}
