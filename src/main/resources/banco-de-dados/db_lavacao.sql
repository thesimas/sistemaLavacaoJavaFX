drop database if exists db_lavacao;
create database if not exists db_lavacao;
use db_lavacao;

create table parametros_sistema
(
    id     int not null primary key,
    pontos int
) engine = innoDB;

create table cor
(
    id   int         not null auto_increment,
    nome varchar(55) not null,
    constraint pk_cor
        primary key (id)

) engine = innoDB;

create table marca
(
    id   int         not null auto_increment,
    nome varchar(55) not null,
    constraint pk_marca
        primary key (id)

) engine = innoDB;

CREATE TABLE servico (
                         id        INT NOT NULL AUTO_INCREMENT,
                         descricao VARCHAR(55) NOT NULL,
                         valor     DOUBLE NOT NULL,
                         categoria ENUM ('PEQUENO', 'MEDIO', 'GRANDE', 'MOTO', 'PADRAO') DEFAULT 'PADRAO',
                         CONSTRAINT pk_servico PRIMARY KEY (id)
) ENGINE = InnoDB;

create table modelo
(
    id        int         not null auto_increment,
    descricao varchar(55) not null,
    categoria ENUM ('PEQUENO', 'MEDIO', 'GRANDE', 'MOTO', 'PADRAO') DEFAULT 'PADRAO',
    id_marca  int,
    constraint pk_modelo primary key (id),
    constraint id_marca foreign key (id_marca) references marca (id)
) engine = innoDB;

create table motor
(
    id_modelo        int primary key,
    potencia         int,
    tipo_combustivel ENUM ('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO') DEFAULT 'GASOLINA'
) engine = innoDB;

INSERT INTO cor(nome)
VALUES ('Prata');
INSERT INTO cor(nome)
VALUES ('Vermelho');
INSERT INTO cor(nome)
VALUES ('Cinza');
INSERT INTO cor(nome)
VALUES ('Grafite');
INSERT INTO cor(nome)
VALUES ('Amarelo');

INSERT INTO marca(nome)
VALUES ('Chevrolet');
INSERT INTO marca(nome)
VALUES ('Honda');
INSERT INTO marca(nome)
VALUES ('Ford');
INSERT INTO marca(nome)
VALUES ('Hyundai');
INSERT INTO marca(nome)
VALUES ('Renault');
INSERT INTO marca(nome)
VALUES ('Jeep');

INSERT INTO servico(descricao, valor, categoria)
VALUES ('Polimento Cristalizado', 350.00, 'PADRAO');
INSERT INTO servico(descricao, valor, categoria)
VALUES ('Higienização Interna', 250.00, 'PADRAO');
INSERT INTO servico(descricao, valor, categoria)
VALUES ('Lavação de Motor', 80.00, 'PADRAO');
INSERT INTO servico(descricao, valor, categoria)
VALUES ('Lavação Moto Simples', 40.00, 'MOTO');
INSERT INTO servico(descricao, valor, categoria)
VALUES ('Lavação Caminhonete', 180.00, 'GRANDE');

INSERT INTO parametros_sistema(id, pontos)
VALUES (1, 20);

INSERT INTO modelo (descricao, categoria, id_marca) VALUES ('Onix', 'PEQUENO', 1);
INSERT INTO modelo (descricao, categoria, id_marca) VALUES ('Civic', 'MEDIO', 2);
INSERT INTO modelo (descricao, categoria, id_marca) VALUES ('Ranger', 'GRANDE', 3);
INSERT INTO modelo (descricao, categoria, id_marca) VALUES ('HB20', 'PEQUENO', 4);
INSERT INTO modelo (descricao, categoria, id_marca) VALUES ('Compass', 'GRANDE', 6);

INSERT INTO motor (id_modelo, potencia, tipo_combustivel) VALUES (1, 116, 'FLEX');
INSERT INTO motor (id_modelo, potencia, tipo_combustivel) VALUES (2, 155, 'FLEX');
INSERT INTO motor (id_modelo, potencia, tipo_combustivel) VALUES (3, 200, 'DIESEL');
INSERT INTO motor (id_modelo, potencia, tipo_combustivel) VALUES (4, 120, 'FLEX');
INSERT INTO motor (id_modelo, potencia, tipo_combustivel) VALUES (5, 185, 'FLEX');