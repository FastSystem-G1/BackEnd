CREATE DATABASE FastSystem;
USE FastSystem;

DROP DATABASE FastSystem;

CREATE TABLE Empresa(
id_empresa INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
nome_empresa VARCHAR(100),
cnpj_empresa VARCHAR (14),
cep_empresa VARCHAR(8),
numero_empresa INT,
telefone_empresa VARCHAR(13),
nome_representante VARCHAR(100),
email_empresa VARCHAR(50),
senha_empresa VARCHAR(25)
)AUTO_INCREMENT = 0;

SELECT * FROM Empresa;

INSERT INTO empresa VALUES 
(null, 'FastSystem', 123456789, 02535412, 1522, 11942563656, "Endryl", "endryl@gmail.com", 12345678),
(null, 'McDonalds', 987654321, 32654845, 365, 11953145796, "Donald McDonalds", 'dodo@gmail.com', 12345678);

-- CREATE TABLE Funcionario(
-- id_funcionario INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
-- fk_empresa INT,
-- nome_funcionario VARCHAR(100),
-- is_admin BOOLEAN,
-- cpf_funcionario VARCHAR(11),
-- email_funcionario VARCHAR(50),
-- senha_funcionario VARCHAR(25),
-- telefone_funcionario VARCHAR(13),
-- FOREIGN KEY(fk_empresa) REFERENCES Empresa(id_empresa)
-- )AUTO_INCREMENT = 100;

CREATE TABLE Maquina(
id_maquina INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
fk_empresa INT,
tipo_maquina VARCHAR(7),
CHECK (tipo_maquina = 'DESKTOP' or tipo_maquina = 'TOTEM'),
nome_maquina VARCHAR(15),
sistema_operacional_maquina varchar(45),
tempo_atividade_maquina LONG,
FOREIGN KEY(fk_empresa) REFERENCES Empresa(id_empresa)
)AUTO_INCREMENT = 0;

INSERT INTO Maquina VALUES 
( null, 1, "DESKTOP", "Desktop 1", "", 0 ),  
( null, 2, "TOTEM", "Totem 1", "", 0 ),
( null, 2, "DESKTOP", "Desktop 1", "", 0 );

-- CREATE TABLE App(
-- id_app INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
-- nome_app VARCHAR(60),
-- funcao VARCHAR(45),
-- prioridade INT
-- )AUTO_INCREMENT = 1000;

-- CREATE TABLE App_Maquina(
-- fk_maquina INT,
-- fk_app INT,
-- FOREIGN KEY(fk_maquina) REFERENCES Maquina(id_maquina),
-- FOREIGN KEY(fk_app) REFERENCES App(id_app)
-- );

-- create table Registro_App(
-- 	idRegistroApp int primary key auto_increment,
--     nome_app varchar(45),
--     fk_maquina int,
--     foreign key(fk_maquina) references Maquina(id_maquina),
--     fk_empresa int,
--     foreign key(fk_empresa) references empresa(id_empresa)
-- );

CREATE TABLE Componente(
id_componente INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
nome_componente VARCHAR(45),
is_ativo BOOLEAN,
fabricante_componente VARCHAR(45),
modelo_componente VARCHAR(100),
capacidade_componente INT
);

CREATE TABLE Componente_Maquina(
fk_componente INT,
fk_maquina INT,
FOREIGN KEY(fk_componente) REFERENCES Componente(id_componente),
FOREIGN KEY(fk_maquina) REFERENCES Maquina(id_maquina)
);
    
CREATE TABLE Tipo_Registro(
id_tipo_registro INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
descricao_tipo VARCHAR(20)
);

INSERT INTO Tipo_Registro VALUES
( null, 'GB' ),
( null, '%' );

CREATE TABLE Registro(
fk_componente INT,
fk_maquina INT,
data_hora DATETIME,
medida FLOAT,
fk_tipo_registro INT,
FOREIGN KEY(fk_componente) REFERENCES Componente(id_componente),
FOREIGN KEY(fk_maquina) REFERENCES Maquina(id_maquina),
FOREIGN KEY(fk_tipo_registro) REFERENCES Tipo_Registro(id_tipo_registro)
);


-- Faça esses selects
SELECT nome_empresa, nome_maquina, nome_componente FROM Empresa
	INNER JOIN Maquina ON Empresa.id_empresa = maquina.fk_empresa
	INNER JOIN Componente_Maquina ON Maquina.id_maquina = Componente_Maquina.fk_maquina
    INNER JOIN Componente ON Componente.id_componente = Componente_Maquina.fk_componente;
    
SELECT * FROM Maquina;


-- TESTES
SELECT * FROM Componente;
TRUNCATE Componente;
DROP TABLE Componente;

-- UPDATE Componente SET nome_componente = "", is_ativo = true, fabricante_componente = "", modelo_componente = "", capacidade_componente = 0 WHERE id_componenete = 1;

-- Processador
INSERT INTO Componente VALUES
( null, 'Processador', true, "GenuineIntel", 'Intel(R) Core(TM) i3-4005U CPU @ 1.70GHz', 100 );
INSERT INTO Componente VALUES
( null, 'Processador', true, "GenuineIntel", 'Intel(R) Core(TM) i3-4005U CPU @ 1.70GHz', 100 );
-- Memória
INSERT INTO Componente (id_componente, nome_componente, is_ativo, capacidade_componente) VALUES
( null, 'Memória', true, 100 );
-- Disco
INSERT INTO Componente (id_componente, nome_componente, is_ativo, modelo_componente, capacidade_componente) VALUES
( null, '\\.\PHYSICALDRIVE0', true, 'KINGSTON SA400S37240G (Unidades de disco padrão)', 240 );

SELECT * FROM Componente;
SELECT * FROM Maquina;
DROP TABLE Componente_Maquina;
SELECT * FROM  Componente_Maquina;

INSERT INTO Componente_Maquina VALUES
( 1, 1 ),
( 3, 1 ),
( 4, 1 ),
( 2, 2 );

SELECT * FROM Registro;
TRUNCATE Registro;
DROP TABLE Registro;

INSERT INTO Registro VALUES
( 1, 1, '2022-10-14 20:17:06', '10', 1 );

-- SELECTS

SELECT id_empresa, id_maquina FROM Maquina
	INNER JOIN empresa ON empresa.id_empresa = maquina.fk_empresa
		WHERE nome_maquina = 'Desktop 1' and email_empresa = 'endryl@gmail.com' and senha_empresa = 12345678;

SELECT nome_empresa, id_maquina FROM Empresa
	INNER JOIN Maquina ON Empresa.id_empresa = maquina.fk_empresa;

SELECT nome_empresa, nome_maquina, nome_componente FROM Empresa
	INNER JOIN Maquina ON Empresa.id_empresa = maquina.fk_empresa
	INNER JOIN Componente_Maquina ON Maquina.id_maquina = Componente_Maquina.fk_maquina
    INNER JOIN Componente ON Componente.id_componente = Componente_Maquina.fk_componente
		WHERE id_empresa = 1 and id_maquina = 1;
        
SELECT nome_maquina, id_maquina, fk_componente FROM Componente
	INNER JOIN Componente_Maquina ON Componente_Maquina.fk_componente = Componente.id_componente
	INNER JOIN Maquina ON Maquina.id_maquina = Componente_Maquina.fk_maquina
		WHERE id_maquina = 1;
        
SELECT nome_maquina, id_maquina, fk_componente FROM Componente_Maquina
	INNER JOIN Maquina ON Maquina.id_maquina = Componente_Maquina.fk_maquina
		WHERE id_maquina = 1;
    

/* para sql server - remoto - produção 
CREATE TABLE usuario (
	id INT PRIMARY KEY IDENTITY(1,1),
	nome VARCHAR(50),
	email VARCHAR(50),
	senha VARCHAR(50),
);

CREATE TABLE aviso (
	id INT PRIMARY KEY IDENTITY(1,1),
	titulo VARCHAR(100),
	descricao VARCHAR(150),
	fk_usuario INT FOREIGN KEY REFERENCES usuario(id)
);

create table aquario (
-- em nossa regra de negócio, um aquario tem apenas um sensor
	id INT PRIMARY KEY IDENTITY(1,1),
	descricao VARCHAR(300)
);

-- altere esta tabela de acordo com o que está em INSERT de sua API do arduino

CREATE TABLE medida (
	id INT PRIMARY KEY IDENTITY(1,1),
	dht11_umidade DECIMAL,
	dht11_temperatura DECIMAL,
	luminosidade DECIMAL,
	lm35_temperatura DECIMAL,
	chave TINYINT,
	momento DATETIME,
	fk_aquario INT FOREIGN KEY REFERENCES aquario(id)
);
*/