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
    constraint pk_cor primary key (id)

) engine = innoDB;

create table marca
(
    id   int         not null auto_increment,
    nome varchar(55) not null,
    constraint pk_marca primary key (id)

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
    constraint fk_marca foreign key (id_marca) references marca (id)
) engine = innoDB;

create table motor
(
    id_modelo        int not null references modelo(id),
    potencia         int,
    tipo_combustivel ENUM ('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO') DEFAULT 'GASOLINA',
    CONSTRAINT pk_id_modelo PRIMARY KEY (id_modelo),
    CONSTRAINT fk_id_modelo FOREIGN KEY (id_modelo) REFERENCES modelo(id)
        ON DELETE CASCADE
) engine = innoDB;

CREATE TABLE cliente (
    id int primary key auto_increment,
    nome varchar(255) not null,
    celular varchar(55),
    email varchar(100),
    data_cadastro date
)Engine = innoDB;

CREATE TABLE pessoaFisica (
    id_cliente int not null references cliente(id),
    cpf varchar(20) not null,
    data_nascimento date,
    CONSTRAINT pk_pessoaFisica PRIMARY KEY (id_cliente),
    CONSTRAINT fk_pessoaFisica FOREIGN KEY (id_cliente) REFERENCES cliente(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
)Engine = innoDB;

CREATE TABLE pessoaJuridica (
     id_cliente int not null references cliente(id),
     cnpj varchar(20) not null,
     inscricao_estadual varchar(80),
     CONSTRAINT pk_pessoaJuridica PRIMARY KEY (id_cliente),
     CONSTRAINT fk_pessoaJuridica FOREIGN KEY (id_cliente) REFERENCES cliente(id)
             ON UPDATE CASCADE
             ON DELETE CASCADE
)Engine = innoDB;

CREATE TABLE pontuacao (
    id_cliente int not null references cliente(id),
    quantidade int not null,
    CONSTRAINT pk_pontuacao PRIMARY KEY (id_cliente),
    CONSTRAINT fk_pontuacao FOREIGN KEY (id_cliente) REFERENCES cliente(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
)Engine = innodb;

CREATE TABLE veiculo
(
    id  int     not null auto_increment primary key,
    placa varchar(8) not null,
    observacoes varchar(255),
    id_cor int,
    id_modelo int,
    id_cliente int,
    constraint fk_cliente foreign key (id_cliente) references cliente (id),
    constraint fk_modelo foreign key (id_modelo) references modelo (id),
    constraint fk_cor foreign key (id_cor) references cor (id)

)engine = innoDB;

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

INSERT INTO cliente (id, nome, celular, email, data_cadastro) VALUES
(1, 'Luciano Silva', '(48) 99999-1111', 'luciano.silva@email.com', '2026-05-01'),
(2, 'Marina Costa', '(48) 98888-2222', 'marina.costa@email.com', '2026-05-02'),
(3, 'Locadora Floripa Rent a Car', '(48) 3333-4444', 'contato@floriparentacar.com.br', '2026-05-03'),
(4, 'Transportes Sul Ltda', '(48) 3222-5555', 'frota@transportessul.com.br', '2026-05-04');

INSERT INTO pessoaFisica (id_cliente, cpf, data_nascimento) VALUES
(1, '111.222.333-44', '1998-08-15'),
(2, '555.666.777-88', '1985-03-22');

INSERT INTO pessoaJuridica (id_cliente, cnpj, inscricao_estadual) VALUES
(3, '12.345.678/0001-99', '123456789'),
(4, '98.765.432/0001-11', '987654321');

INSERT INTO pontuacao (id_cliente, quantidade) VALUES
(1, 0),
(2, 50),
(3, 120),
(4, 0);

INSERT INTO veiculo (placa, observacoes, id_cor, id_modelo, id_cliente) VALUES
('ABC-1234', 'Carro de uso diário', 1, 1, 1),                 -- Veículo do Luciano (Onix Prata)
('XYZ-9B76', 'Arranhão na porta lateral direita', 2, 2, 2), -- Veículo da Marina (Civic Vermelho)
('DEF-5678', 'Caminhonete de frota', 3, 3, 3),              -- Veículo 1 da Locadora (Ranger Cinza)
('GHI-9D12', 'Veículo locado - plano mensal', 4, 4, 3),     -- Veículo 2 da Locadora (HB20 Grafite) -> Relação 1:N
('JKL-3456', 'Uso da diretoria', 5, 5, 4);                  -- Veículo da Transportes Sul (Compass Amarelo)