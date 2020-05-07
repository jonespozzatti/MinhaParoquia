INSERT INTO `hibernate_sequence` (`sequence_name`, `next_val`) VALUES ('', '1');
INSERT INTO `paroquia` (`id`, `cnpj`, `data_atualizacao`, `data_criacao`, `email`,`localizacao`, `razao_social`, `endereco_id`) 
VALUES (NULL, '53608206000165', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'jones.pozzatti@gmail.com', NULL, 'Santa Izabel', NULL);

INSERT INTO `pessoa` (  `cpf`, `data_atualizacao`, `data_criacao`, `datanasc`, `email`, `nome`, `perfil`, `senha`, `sexo`, `telefonecelular`, `telefonefixo`, `estadocivil`, `endereco_id`, `responsavel_id`) VALUES
('03138358968', '2020-04-26 18:00:26', '2020-04-26 18:00:26', '2020-04-26 18:00:26', 'usuario@email.com', 'Joao', 'ROLE_USUARIO', '$2a$10$3LoD5.SJMyy/boHjUrIahuROKSA8tQrcbNsiOdY3Q/tjixs.pK8GO', NULL, NULL, NULL, NULL, NULL, NULL),
( '03138358969', '2020-04-26 18:00:27', '2020-04-26 18:00:27', '2020-04-26 18:00:27', 'admin@email.com', 'Jones', 'ROLE_ADMIN', '$2a$10$4iTVGDtLmOSdqNgTJInzR.92ajDlxb.u2KXbQ1WUgh79GMEwwvmui', NULL, NULL, NULL,  NULL, NULL, NULL);

INSERT INTO `pastoral` (`id`, `data_atualizacao`, `data_criacao`, `descricao`, `email`, `nome`, `paroquia_id`) VALUES
(2, '2020-05-02 23:15:14', '2020-05-02 23:15:14', 'Paroquiano Santa Izabel', 'paroquiasantaizabel@gmail.com', 'Paroquiano', 1);

INSERT INTO `pessoapastoral` (`id`, `tipoparticipantepastoral`, `pastoral_id`, `pessoa_id`) VALUES
(1, 0, 2, 1);
