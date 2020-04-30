package org.paroquia.api.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.paroquia.api.entities.Paroquia;
import org.paroquia.api.sevices.ParoquiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ParoquiaControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ParoquiaService paroquiaService;

	private static final String BUSCAR_PAROQUIA_ID_URL = "/api/paroquia/";
	private static final Long ID = Long.valueOf(1);
	private static final String CNPJ = "83475939000130";
	private static final String RAZAO_SOCIAL = "Paroquia XYZ";
	private static final String EMAIL = "teste@teste.com.br";
	@Test
	@WithMockUser
	public void testBuscarParoquiaCnpjInvalido() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_PAROQUIA_ID_URL + ID).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Paroquia nao encontrada")));
	}

	@Test
	@WithMockUser
	public void testBuscarParoquiaCnpjValido() throws Exception {
		
		BDDMockito.given(this.paroquiaService.buscarParoquiaPorId(Mockito.anyLong()))
				.willReturn(Optional.of(this.obterDadosParoquia()));
		
		
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_PAROQUIA_ID_URL + ID)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID))
				.andExpect(jsonPath("$.data.razaoSocial", equalTo(RAZAO_SOCIAL)))
				.andExpect(jsonPath("$.data.cnpj", equalTo(CNPJ)))
				.andExpect(jsonPath("$.data.email", equalTo(EMAIL)))
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	private Paroquia obterDadosParoquia() {
		Paroquia paroquia = new Paroquia();
		paroquia.setId(ID);
		paroquia.setEmail(EMAIL);
		paroquia.setRazaoSocial(RAZAO_SOCIAL);
		paroquia.setCnpj(CNPJ);
		return paroquia;
	}

}