 
create table endereco (
       id bigint not null,
        bairro varchar(255),
        cep varchar(255),
        cidade varchar(255),
        logradouro varchar(255),
        uf varchar(255),
        primary key (id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
 alter table endereco modify id bigint not null AUTO_INCREMENT;

create table paroquia (
        id bigint not null,
        cnpj varchar(255) not null,
        data_atualizacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        data_criacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        localizacao varchar(255),
        razao_social varchar(255) not null,
        endereco_id bigint,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


alter table paroquia 
       add constraint FK_endereco_paroquia 
       foreign key (endereco_id) 
       references endereco(id);
 
alter table paroquia modify id bigint not null AUTO_INCREMENT;

       
create table pastoral (
       id bigint not null,
        data_atualizacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        data_criacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        descricao varchar(255),
        nome varchar(255) not null,
        tipo varchar(255) not null,
        paroquia_id bigint,
        primary key (id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
 alter table pastoral modify id bigint not null AUTO_INCREMENT;
    
     alter table pastoral 
       add constraint FK_paroquia_pastoral
       foreign key (paroquia_id) 
       references paroquia(id);
       
 create table paroquiano (
       id bigint not null,
        cpf varchar(255) not null,
        data_atualizacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        data_criacao timestamp not null DEFAULT CURRENT_TIMESTAMP,
        datanasc timestamp not null DEFAULT CURRENT_TIMESTAMP,
        email varchar(255) not null,
        nome varchar(255) not null,
        perfil varchar(255) not null,
        senha varchar(255) not null,
        telefonecelular varchar(255),
        telefonefixo varchar(255),
        endereco_id bigint,
        paroquia_id bigint,
        pastoral_id bigint,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
 alter table paroquiano modify id bigint not null AUTO_INCREMENT;
 
 alter table paroquiano 
       add constraint FK_endereco_paroquiano
       foreign key (endereco_id) 
       references endereco(id);

 alter table paroquiano 
       add constraint FK_paroquia_paroquiano 
       foreign key (paroquia_id) 
       references paroquia(id);


 alter table paroquiano 
       add constraint FK_pastoral_paroquiano 
       foreign key (pastoral_id) 
       references pastoral(id);
