INSERT INTO `hibernate_sequence` (`sequence_name`, `next_val`) VALUES ('', '1');
INSERT INTO `paroquia` (`id`, `cnpj`, `data_atualizacao`, `data_criacao`, `email`,`localizacao`, `razao_social`, `endereco_id`) 
VALUES (NULL, '53608206000165', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'jones.pozzatti@gmail.com', NULL, 'Santa Izabel', NULL);

INSERT INTO `pessoa` (`tipo`, `id`, `cpf`, `data_atualizacao`, `data_criacao`, `datanasc`, `email`, `nome`, `perfil`, `senha`, `sexo`, `telefonecelular`, `telefonefixo`, `situacao`, `estadocivil`, `endereco_id`, `responsavel_id`) VALUES
('PESSOA', 1, '03138358968', '2020-04-26 18:00:26', '2020-04-26 18:00:26', '2020-04-26 18:00:26', 'usuario@email.com', 'Joao', 'ROLE_USUARIO', '$2a$10$3LoD5.SJMyy/boHjUrIahuROKSA8tQrcbNsiOdY3Q/tjixs.pK8GO', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('PESSOA', 2, '03138358969', '2020-04-26 18:00:27', '2020-04-26 18:00:27', '2020-04-26 18:00:27', 'admin@email.com', 'Jones', 'ROLE_ADMIN', '$2a$10$4iTVGDtLmOSdqNgTJInzR.92ajDlxb.u2KXbQ1WUgh79GMEwwvmui', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
