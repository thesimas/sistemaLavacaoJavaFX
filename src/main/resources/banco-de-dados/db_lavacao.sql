drop database if exists db_lavacao;
create database if not exists db_lavacao;
use db_lavacao;

create table configuracao_sistema
(
    id                  int not null primary key auto_increment,
    pontos              int,
    porcentagem_pequeno float,
    porcentagem_medio   float,
    porcentagem_grande  float,
    porcentagem_moto    float
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

CREATE TABLE servico
(
    id        INT         NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(55) NOT NULL,
    valor     DOUBLE      NOT NULL,
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
    id_modelo        int not null references modelo (id),
    potencia         int,
    tipo_combustivel ENUM ('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO') DEFAULT 'GASOLINA',
    CONSTRAINT pk_id_modelo PRIMARY KEY (id_modelo),
    CONSTRAINT fk_id_modelo FOREIGN KEY (id_modelo) REFERENCES modelo (id)
        ON DELETE CASCADE
) engine = innoDB;

CREATE TABLE cliente
(
    id            int primary key auto_increment,
    nome          varchar(255) not null,
    celular       varchar(55),
    email         varchar(100),
    data_cadastro date
) Engine = innoDB;

CREATE TABLE pessoa_fisica
(
    id_cliente      int         not null references cliente (id),
    cpf             varchar(20) not null,
    data_nascimento date,
    CONSTRAINT pk_pessoaFisica PRIMARY KEY (id_cliente),
    CONSTRAINT fk_pessoaFisica FOREIGN KEY (id_cliente) REFERENCES cliente (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) Engine = innoDB;

CREATE TABLE pessoa_juridica
(
    id_cliente         int         not null references cliente (id),
    cnpj               varchar(20) not null,
    inscricao_estadual varchar(80),
    CONSTRAINT pk_pessoaJuridica PRIMARY KEY (id_cliente),
    CONSTRAINT fk_pessoaJuridica FOREIGN KEY (id_cliente) REFERENCES cliente (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) Engine = innoDB;

CREATE TABLE pontuacao
(
    id_cliente int not null references cliente (id),
    quantidade int not null,
    CONSTRAINT pk_pontuacao PRIMARY KEY (id_cliente),
    CONSTRAINT fk_pontuacao FOREIGN KEY (id_cliente) REFERENCES cliente (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) Engine = innodb;

CREATE TABLE veiculo
(
    id          int        not null auto_increment primary key,
    placa       varchar(8) not null unique,
    observacoes varchar(255),
    id_cor      int,
    id_modelo   int,
    id_cliente  int,
    constraint fk_cliente foreign key (id_cliente) references cliente (id),
    constraint fk_modelo foreign key (id_modelo) references modelo (id),
    constraint fk_cor foreign key (id_cor) references cor (id)

) engine = innoDB;

CREATE TABLE ordem_de_servico
(
    numero           BIGINT PRIMARY KEY UNIQUE,
    total            DOUBLE,
    data_agendamento DATE,
    desconto         DOUBLE,
    status           ENUM ('ABERTA', 'FECHADA', 'CANCELADA') DEFAULT 'ABERTA',
    id_veiculo       int NOT NULL,
    constraint fk_veiculo foreign key (id_veiculo) references veiculo (id)
) engine = innoDB;

CREATE TABLE item_os
(
    id                  INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    numero_ordem_de_servico BIGINT NOT NULL,
    valor_servico       DOUBLE,
    observacoes         VARCHAR(255),
    id_servico          INT,

    CONSTRAINT fk_servico FOREIGN KEY (id_servico) REFERENCES servico (id),
    CONSTRAINT fk_item_os FOREIGN KEY (numero_ordem_de_servico) REFERENCES ordem_de_servico (numero)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE = InnoDB;

INSERT INTO cor(nome)
VALUES ('Prata'),
       ('Vermelho'),
       ('Cinza'),
       ('Grafite'),
       ('Amarelo'),
       ('Branco'),
       ('Preto'),
       ('Azul'),
       ('Verde'),
       ('Marrom');

INSERT INTO marca(nome)
VALUES ('Chevrolet'),
       ('Honda'),
       ('Ford'),
       ('Hyundai'),
       ('Renault'),
       ('Jeep');

INSERT INTO servico(descricao, valor, categoria)
VALUES ('Polimento Cristalizado', 350.00, 'PADRAO'),
       ('Higienização Interna', 250.00, 'PADRAO'),
       ('Lavação de Motor', 80.00, 'PADRAO'),
       ('Lavação Moto Simples', 40.00, 'MOTO'),
       ('Polimento Técnico', 750.00, 'PADRAO'),
       ('Espelhamento de Pintura', 550.00, 'PADRAO'),
       ('Vitrificação de Pintura', 1200.00, 'PADRAO'),
       ('Revitalização de Plásticos Externos', 80.00, 'PADRAO'),
       ('Revitalização de Faróis', 150.00, 'PADRAO'),
       ('Hidratação de Bancos em Couro', 180.00, 'PADRAO'),
       ('Hidratação de Plásticos Internos', 70.00, 'PADRAO'),
       ('Limpeza de Teto', 90.00, 'PADRAO'),
       ('Remoção de Chuva Ácida', 180.00, 'PADRAO'),
       ('Pretinho nos Pneus', 30.00, 'PADRAO'),
       ('Lavação Simples', 85.00, 'PADRAO'),
       ('Lavação Completa', 90.00, 'PADRAO'),
       ('Lavação Caminhonete', 180.00, 'GRANDE');

INSERT INTO configuracao_sistema (pontos, porcentagem_pequeno, porcentagem_medio, porcentagem_grande, porcentagem_moto)
VALUES (20, 0, 0, 0, 0);

INSERT INTO modelo (descricao, categoria, id_marca)
VALUES ('Onix', 'PEQUENO', 1),
       ('Civic', 'MEDIO', 2),
       ('Ranger', 'GRANDE', 3),
       ('HB20', 'PEQUENO', 4),
       ('Compass', 'GRANDE', 6),
       ('Cruze', 'MEDIO', 1),
       ('Tracker', 'MEDIO', 1),
       ('S10', 'GRANDE', 1),
       ('Spin', 'MEDIO', 1),
       ('Montana', 'MEDIO', 1),
       ('City', 'PEQUENO', 2),
       ('HR-V', 'MEDIO', 2),
       ('Fit', 'PEQUENO', 2),
       ('CR-V', 'GRANDE', 2),
       ('WR-V', 'PEQUENO', 2),
       ('Ka', 'PEQUENO', 3),
       ('Fiesta', 'PEQUENO', 3),
       ('EcoSport', 'MEDIO', 3),
       ('Territory', 'GRANDE', 3),
       ('Maverick', 'GRANDE', 3),
       ('Creta', 'MEDIO', 4),
       ('Tucson', 'GRANDE', 4),
       ('Santa Fe', 'GRANDE', 4),
       ('HB20S', 'PEQUENO', 4),
       ('i30', 'MEDIO', 4),
       ('Kwid', 'PEQUENO', 5),
       ('Sandero', 'PEQUENO', 5),
       ('Duster', 'MEDIO', 5),
       ('Logan', 'PEQUENO', 5),
       ('Oroch', 'MEDIO', 5),
       ('Renegade', 'MEDIO', 6),
       ('Commander', 'GRANDE', 6),
       ('Wrangler', 'GRANDE', 6),
       ('Gladiator', 'GRANDE', 6),
       ('Grand Cherokee', 'GRANDE', 6);

INSERT INTO motor (id_modelo, potencia, tipo_combustivel)
VALUES (1, 116, 'FLEX'),
       (2, 155, 'FLEX'),
       (3, 200, 'DIESEL'),
       (4, 120, 'FLEX'),
       (5, 185, 'FLEX'),
       (6, 153, 'FLEX'),
       (7, 133, 'FLEX'),
       (8, 200, 'DIESEL'),
       (9, 111, 'FLEX'),
       (10, 133, 'FLEX'),
       (11, 126, 'FLEX'),
       (12, 177, 'FLEX'),
       (13, 116, 'FLEX'),
       (14, 190, 'GASOLINA'),
       (15, 116, 'FLEX'),
       (16, 85, 'FLEX'),
       (17, 125, 'FLEX'),
       (18, 137, 'FLEX'),
       (19, 150, 'GASOLINA'),
       (20, 253, 'GASOLINA'),
       (21, 120, 'FLEX'),
       (22, 177, 'GASOLINA'),
       (23, 280, 'GASOLINA'),
       (24, 120, 'FLEX'),
       (25, 150, 'GASOLINA'),
       (26, 71, 'FLEX'),
       (27, 118, 'FLEX'),
       (28, 120, 'FLEX'),
       (29, 82, 'FLEX'),
       (30, 170, 'FLEX'),
       (31, 185, 'FLEX'),
       (32, 170, 'DIESEL'),
       (33, 272, 'GASOLINA'),
       (34, 284, 'GASOLINA'),
       (35, 380, 'GASOLINA');

INSERT INTO cliente (nome, celular, email, data_cadastro)
VALUES ('Luciano Silva', '(48) 99999-1111', 'luciano.silva@email.com', '2026-05-01'),
       ('Marina Costa', '(48) 98888-2222', 'marina.costa@email.com', '2026-05-02'),
       ('Locadora Floripa Rent a Car', '(48) 3333-4444', 'contato@floriparentacar.com.br', '2026-05-03'),
       ('Transportes Sul Ltda', '(48) 3222-5555', 'frota@transportessul.com.br', '2026-05-04'),
       ('Carlos Eduardo', '(48) 97777-1111', 'carlos.edu@email.com', '2026-05-10'),
       ('Ana Beatriz', '(48) 97777-2222', 'ana.bia@email.com', '2026-05-11'),
       ('Marcos Paulo', '(48) 97777-3333', 'marcos.paulo@email.com', '2026-05-11'),
       ('Julia Mendes', '(48) 97777-4444', 'julia.mendes@email.com', '2026-05-12'),
       ('Uber Frotas SC', '(48) 3222-1111', 'contato@uberfrotas.com.br', '2026-05-12'),
       ('Táxi Coorp VIP', '(48) 3222-2222', 'gerencia@taxicoorp.com.br', '2026-05-13'),
       ('Logística Express', '(48) 3222-3333', 'frota@logexpress.com.br', '2026-05-13'),
       ('Executivo Transportes', '(48) 3222-4444', 'admin@executivotransp.com.br', '2026-05-14');

INSERT INTO pessoa_fisica (id_cliente, cpf, data_nascimento)
VALUES (1, '111.222.333-44', '1998-08-15'),
       (2, '555.666.777-88', '1985-03-22'),
       (5, '111.999.888-77', '1990-01-20'),
       (6, '222.888.777-66', '1985-06-15'),
       (7, '333.777.666-55', '1978-11-30'),
       (8, '444.666.555-44', '2001-03-10');

INSERT INTO pessoa_juridica (id_cliente, cnpj, inscricao_estadual)
VALUES (3, '12.345.678/0001-99', '123456789'),
       (4, '98.765.432/0001-11', '987654321'),
       (9, '11.222.333/0001-44', '111222333'),
       (10, '55.666.777/0001-88', '555666777'),
       (11, '99.888.777/0001-66', '999888777'),
       (12, '44.333.222/0001-11', '444333222');

INSERT INTO pontuacao (id_cliente, quantidade)
VALUES (1, 40),
       (2, 50),
       (3, 120),
       (4, 20),
       (5, 30),
       (6, 15),
       (7, 50),
       (8, 15),
       (9, 200),
       (10, 150),
       (11, 80),
       (12, 350);

INSERT INTO veiculo (placa, observacoes, id_cor, id_modelo, id_cliente)
VALUES ('ABC-1234', 'Carro de uso diário', 1, 1, 1),
       ('XYZ-9B76', 'Arranhão na porta lateral direita', 2, 2, 2),
       ('DEF-5678', 'Caminhonete de frota', 3, 3, 3),
       ('GHI-9D12', 'Veículo locado - plano mensal', 4, 4, 3),
       ('JKL-3456', 'Uso da diretoria', 5, 5, 4),
       ('AAA-1111', 'Pequeno amassado no para-choque', 6, 6, 5),
       ('BBB-2222', 'Lavação interna detalhada', 7, 13, 6),
       ('CCC-3333', 'Sem observações', 8, 31, 7),
       ('DDD-4444', 'Chave presencial', 9, 26, 8),
       ('EEE-5555', 'Veículo 01 da frota Uber', 10, 24, 9),
       ('FFF-6666', 'Veículo 02 da frota Uber', 6, 29, 9),
       ('GGG-7777', 'Táxi VIP - Prioridade', 7, 12, 10),
       ('HHH-8888', 'Furgão de entregas', 6, 28, 11),
       ('III-9999', 'Carro da diretoria (Blindado)', 7, 32, 12),
       ('JJJ-0000', 'Veículo de apoio', 1, 10, 12);

INSERT INTO ordem_de_servico (numero, total, data_agendamento, desconto, status, id_veiculo)
VALUES
    (2026001, 340.00, '2026-05-10', 0.00, 'FECHADA', 1),  -- OS do Onix (Luciano)
    (2026002, 85.00, '2026-05-11', 0.00, 'FECHADA', 2),   -- OS do Civic (Marina)
    (2026003, 85.00, '2026-04-12', 0.00, 'FECHADA', 10),
    (2026004, 430.00, '2026-01-15', 0.00, 'FECHADA', 3),  -- Caminhonete da Locadora
    (2026005, 440.00, '2026-02-10', 0.00, 'FECHADA', 5),  -- Jeep Compass
    (2026006, 85.00, '2026-02-22', 0.00, 'FECHADA', 12),  -- Táxi VIP
    (2026007, 85.00, '2026-03-05', 0.00, 'FECHADA', 15),  -- Veículo de Apoio
    (2026008, 600.00, '2026-04-18', 0.00, 'FECHADA', 8),  -- Cliente Ana Beatriz
    (2026009, 340.00, '2026-06-02', 0.00, 'FECHADA', 14), -- Carro Blindado
    (2026010, 85.00, '2026-07-20', 0.00, 'FECHADA', 9);   -- Chave Presencial

INSERT INTO item_os (numero_ordem_de_servico, valor_servico, observacoes, id_servico)
VALUES
    (2026001, 90.00, 'Lavação completa no capricho', 16),
    (2026001, 250.00, 'Tirar manchas do banco', 2),
    (2026002, 85.00, 'Apenas por fora', 15),
    (2026003, 85.00, 'Lavação rápida padrão', 15),
    (2026004, 180.00, 'Lavação pesada da carroceria', 17),
    (2026004, 250.00, 'Higienização após trilha', 2),
    (2026005, 350.00, 'Polimento para realçar brilho', 1),
    (2026005, 90.00, 'Lavação completa', 16),
    (2026006, 85.00, 'Lavação rápida', 15),
    (2026007, 85.00, 'Padrão', 15),
    (2026008, 350.00, 'Polimento cristalizado premium', 1),
    (2026008, 250.00, 'Higienização para tirar cheiro de cigarro', 2),
    (2026009, 90.00, 'Lavação completa', 16),
    (2026009, 250.00, 'Limpeza dos dutos de ar', 2),
    (2026010, 85.00, 'Veículo de aplicativo, foco interno', 15);