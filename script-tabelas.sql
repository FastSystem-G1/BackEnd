CREATE DATABASE FastSystem;
USE FastSystem;

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
(null, 'FastSystem', 123456789, 02535412, 1522, 11942563656, "Endryl", "endryl@gmail.com", "12345678"),
(null, 'McDonalds', 987654321, 32654845, 365, 11953145796, "Donald McDonalds", 'Dodo@gmail.com', '87654321');

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

INSERT INTO Maquina (id_maquina, fk_empresa, tipo_maquina, nome_maquina) VALUES 
( null, 1, "DESKTOP", "Desktop 1" ),  
( null, 2, "TOTEM", "Totem 1" ),
( null, 2, "DESKTOP", "Desktop 1" );

SELECT id_empresa, id_maquina FROM Maquina
	INNER JOIN empresa ON empresa.id_empresa = maquina.fk_empresa
		WHERE nome_maquina = 'Desktop 1' and email_empresa = 'endryl@gmail.com' and senha_empresa = 12345678;

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

CREATE TABLE Componente(
id_componente INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
nome_componente VARCHAR(45),
is_ativo BOOLEAN,
fabricante_componente VARCHAR(45),
modelo_componente VARCHAR(100),
capacidade_componente INT
);

SELECT * FROM Componente;
TRUNCATE Componente;
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

CREATE TABLE Componente_Maquina(
fk_componente INT,
fk_maquina INT,
FOREIGN KEY(fk_componente) REFERENCES Componente(id_componente),
FOREIGN KEY(fk_maquina) REFERENCES Maquina(id_maquina)
);

SELECT * FROM Componente;
SELECT * FROM Maquina;
SELECT * FROM  Componente_Maquina;

INSERT INTO Componente_Maquina VALUES
( 1, 1 ),
( 3, 1 ),
( 4, 1 ),
( 2, 2 );

SELECT nome_empresa, nome_maquina, nome_componente FROM Empresa
	INNER JOIN Maquina ON Empresa.id_empresa = maquina.fk_empresa
	INNER JOIN Componente_Maquina ON Maquina.id_maquina = Componente_Maquina.fk_maquina
    INNER JOIN Componente ON Componente.id_componente = Componente_Maquina.fk_componente;
    
CREATE TABLE Tipo_Registro(
id_tipo_registro INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
descricao_tipo VARCHAR(20)
);

INSERT INTO Tipo_Rsgistro VALUES
( null, 'GB' ),
( null, '%' ),
( null, 'Segundos' );

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

-- INSERT INTO Empresa (nome_empresa, cnpj_empresa, cep_empresa, numero_empresa, telefone_empresa, nome_representante, email_empresa, senha_empresa)
--  VALUES ('McDonalds Augusta', '44729194000136', '03273430', 188, '(11)8486-5515', 'Paulo Muzy', 'mcdonalds188@gmail.com', '12345678'),
-- 		('Popeyes Av.Paulista', '76444561000141', '08474233', 8115, '(11)0568-8515', 'Jorge de Sá', 'popeyes8115@gmail.com', '12345678'),
--         ('McDonalds Av.Paulista', '65708879000176', '04894465', 355, '(11)8941-8115', 'Renato Russo', 'mcdonalds355@gmail.com', '12345678'),
-- 		('KFC Av.Paulista', '57992929000161', '04913140', 885, '(11)8485-6547', 'Ivete Sangalo', 'kfc885@gmail.com', '12345678');

-- SELECT * FROM Empresa;

-- INSERT INTO Funcionario(fk_empresa, nome_funcionario, is_admin, cpf_funcionario, email_funcionario, senha_funcionario, telefone_funcionario)
-- 	 VALUES (2, 'Cleber', true, '55500088833', 'felipe@gmail.com', '12345678', 11984564858);
--      
-- SELECT * FROM Funcionario;

-- SELECT * FROM App;

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