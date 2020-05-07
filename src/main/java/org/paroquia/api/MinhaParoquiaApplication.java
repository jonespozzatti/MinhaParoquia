package org.paroquia.api;

import org.paroquia.api.repositories.ParoquiaRespository;
import org.paroquia.api.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class MinhaParoquiaApplication {


	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ParoquiaRespository paroquiaRespository;
	
	public static void main(String[] args) {
		SpringApplication.run(MinhaParoquiaApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			
			
			
		//	paroquiaService.testarServico();
//			Paroquia paroquia = new Paroquia();
//			paroquia.setRazaoSocial("Kazale IT");
//			paroquia.setCnpj("74645215000104");
//			paroquia.setEmail("paroquia@com.br");
//			Pastoral pastoral = new Pastoral();
//			
//			pastoral.setParoquia(paroquia);
//			pastoral.setDescricao("teste1");
//			pastoral.setEmail("paroquia@com.br");
//			pastoral.setNome("Batismo");
//			pastoral.setDataAtualizacao(new Date());
//						
//			Endereco endereco = new Endereco();
//			endereco.setCep("80320100");
//			endereco.setLogradouro("rua 1");
//			
//			paroquia.setEndereco(endereco);
//			
//			paroquia.getPastorais().add(pastoral);
//			
//			
//			Paroquia p = this.paroquiaRespository.save(paroquia);
//			
//			Pessoa usuario = new Pessoa();
//			usuario.setEmail("usuario@email.com");
//			usuario.setPerfil(PerfilEnum.ROLE_USUARIO);
//			usuario.setSenha(SenhaUtils.gerarBCrypt("123456"));
//			usuario.setNome("Joao");
//			usuario.setCpf("03138358968");
//			usuario.setDataNasc(new Date());
//			
//			this.pessoaRepository.save(usuario);
//			
//			Pessoa admin = new Pessoa();
//			admin.setEmail("admin@email.com");
//			admin.setPerfil(PerfilEnum.ROLE_ADMIN);
//			admin.setSenha(SenhaUtils.gerarBCrypt("123456"));
//			admin.setNome("Jones");
//			admin.setCpf("03138358969");
//			admin.setDataNasc(new Date());
//			
//			this.pessoaRepository.save(admin);
//
//			List<Paroquia> paroquias = paroquiaRespository.findAll();
//			paroquias.forEach(System.out::println);
//			//paroquia.getPastorais().forEach(System.out::println);
//			
//			Optional<Paroquia> paroquiaDb = paroquiaRespository.findById(1L);
//			System.out.println("paroquia por ID: " + paroquiaDb);
//			
//			List<Pastoral> listaPastorais = pastoralRepository.findByParoquia(paroquiaDb.get());
//			
//			paroquiaDb.get().setRazaoSocial("Kazale IT Web");
//			this.paroquiaRespository.save(paroquiaDb.get());
//
//			Paroquia paroquiaCnpj = paroquiaRespository.findByCnpj("74645215000104");
//			System.out.println("paroquia por CNPJ: " + paroquiaCnpj);
//			
//			this.paroquiaRespository.deleteById(1L);
//			paroquias = paroquiaRespository.findAll();
//			System.out.println("paroquias: " + paroquias.size());
//			
		};
	}

}
