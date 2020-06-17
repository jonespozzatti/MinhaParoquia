

CREATE TABLE `hibernate_sequence` (
    `sequence_name` VARCHAR(255) NOT NULL,
    `next_val` INT(19),
    PRIMARY KEY (`sequence_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table curso (
       id bigint not null,
       data_atualizacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        data_criacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        descricao varchar(255),
        nome varchar(255) not null,
        paroquia_id bigint not null,
        primary key (id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table curso modify id bigint not null AUTO_INCREMENT;     
    
create table endereco (
       id bigint not null,
        bairro varchar(255),
        cep varchar(255),
        cidade varchar(255),
        logradouro varchar(255),
        uf varchar(255),
        numero varchar(255),
        primary key (id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table endereco modify id bigint not null AUTO_INCREMENT;
    
    create table matricula (
       id bigint not null,
       	data_atualizacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        data_criacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        observacao varchar(255),
        situacao_matricula integer,
        tipo_pessoa integer not null,
        pessoa_id bigint not null,
        turma_id bigint not null,
        primary key (id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table matricula modify id bigint not null AUTO_INCREMENT;


    create table paroquia (
       id bigint not null,
        cnpj varchar(255) not null,
       data_atualizacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        data_criacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        email varchar(255) not null,
        localizacao varchar(255),
        razao_social varchar(255) not null,
        endereco_id bigint,
        primary key (id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table paroquia modify id bigint not null AUTO_INCREMENT;
 
 create table noticia (
       id bigint not null,
       ativo boolean default true not null,
       data_apresentacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
       data_atualizacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        data_criacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        descricao varchar(255) not null,
        nome varchar(255) not null,
        paroquia_id bigint not null,
        primary key (id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table noticia modify id bigint not null AUTO_INCREMENT; 

  
    create table pastoral (
       id bigint not null,
       data_atualizacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        data_criacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        descricao varchar(255),
        email varchar(255),
        nome varchar(255) not null,
        paroquia_id bigint not null,
        primary key (id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table pastoral modify id bigint not null AUTO_INCREMENT;

    
    create table pessoa (
        id bigint not null,
        cpf varchar(255) not null,
        data_atualizacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        data_criacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        datanasc timestamp not null DEFAULT CURRENT_TIMESTAMP,
        email varchar(255) not null,
        estadocivil integer,
        nome varchar(255) not null,
        perfil varchar(255) not null,
        senha varchar(255) not null,
        sexo integer,
        telefonecelular varchar(255),
        telefonefixo varchar(255),
        endereco_id bigint,
        responsavel_id bigint,
        primary key (id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table pessoa modify id bigint not null AUTO_INCREMENT;

    
    create table pessoapastoral (
       id bigint not null,
        tipoparticipantepastoral integer not null,
        pastoral_id bigint not null,
        pessoa_id bigint not null,
        primary key (id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table pessoapastoral  modify id bigint not null AUTO_INCREMENT;

    
    create table turma (
       id bigint not null,
       data_atualizacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        data_criacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        data_inicio timestamp not null DEFAULT CURRENT_TIMESTAMP,
        data_fim timestamp not null DEFAULT CURRENT_TIMESTAMP,
        descricao varchar(255) not null,
        dia_semana binary(255) not null,
        horarios binary(255) not null,
        curso_id bigint not null,
        primary key (id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table turma modify id bigint not null AUTO_INCREMENT;

alter table curso 
       add constraint FK_paroquia_curso
       foreign key (paroquia_id) 
       references paroquia(id);

alter table matricula 
       add constraint FK_turma_matricula 
       foreign key (turma_id) 
       references turma(id); 
         
alter table matricula 
       add constraint FK_pessoa_matricula
       foreign key (pessoa_id) 
       references pessoa(id);

alter table paroquia 
       add constraint FK_endereco_paroquia 
       foreign key (endereco_id) 
       references endereco(id); 

alter table pastoral 
       add constraint FK_paroquia_pastoral
       foreign key (paroquia_id) 
       references paroquia(id);
       
alter table noticia 
       add constraint FK_paroquia_noticia
       foreign key (paroquia_id) 
       references paroquia(id);

alter table pessoa 
       add constraint FK_endereco_pessoa
       foreign key (endereco_id) 
       references endereco(id);
alter table pessoa 
       add constraint FK_responsavel_pessoa
       foreign key (responsavel_id) 
       references pessoa(id);

alter table pessoapastoral 
       add constraint FK_pastoral_pessoa 
       foreign key (pastoral_id) 
       references pastoral(id);

alter table pessoapastoral 
       add constraint FK_pessoa_pastoral 
       foreign key (pessoa_id) 
       references pessoa(id);

alter table turma 
       add constraint FK_curso_turma
       foreign key (curso_id) 
       references curso(id);
       