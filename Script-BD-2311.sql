CREATE DATABASE FastSystem;
USE FastSystem;

-- DROP DATABASE FastSystem;

CREATE TABLE Empresa(
id_empresa INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
nome_empresa VARCHAR(100),
cnpj_empresa VARCHAR (14),
cep_empresa VARCHAR(11),
numero_empresa INT,
telefone_empresa VARCHAR(13),
representante VARCHAR(100),
email_empresa VARCHAR(50)
)AUTO_INCREMENT = 0;

CREATE TABLE Funcionario(
id_funcionario INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
fk_empresa INT,
nome_funcionario VARCHAR(100),
is_admin BINARY(1),
cpf_funcionario VARCHAR(11),
email_funcionario VARCHAR(50),
senha_funcionario VARCHAR(25),
telefone_funcionario VARCHAR(13),
FOREIGN KEY(fk_empresa) REFERENCES Empresa(id_empresa)
)AUTO_INCREMENT = 100;

CREATE TABLE Maquina(
id_maquina INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
fk_empresa INT,
tipo_maquina VARCHAR(7),
CHECK (tipo_maquina = 'DESKTOP' or tipo_maquina = 'TOTEM'),
nome_maquina VARCHAR(15),
sistema_operacional_maquina varchar(45),
tempo_atividade_maquina LONG,
email_maquina VARCHAR(45),
senha_maquina VARCHAR(45),
FOREIGN KEY(fk_empresa) REFERENCES Empresa(id_empresa)
)AUTO_INCREMENT = 0;

CREATE TABLE App(
id_app INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
nome_app VARCHAR(60)
)AUTO_INCREMENT = 1000;

CREATE TABLE App_Empresa(
fk_empresa INT,
fk_app INT,
FOREIGN KEY(fk_empresa) REFERENCES Empresa(id_empresa),
FOREIGN KEY(fk_app) REFERENCES App(id_app),
PRIMARY KEY (fk_empresa, fk_app)
);

CREATE TABLE Registro_Processo(
id_registro_processo INT PRIMARY KEY AUTO_INCREMENT,
nome_processo VARCHAR(45),
data_hora DATETIME,
is_autorizado BOOLEAN,
fk_maquina INT,
FOREIGN KEY (fk_maquina) references Maquina(id_maquina)
);

CREATE TABLE Componente(
id_componente INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
nome_componente VARCHAR(45),
is_ativo BINARY(4),
fabricante_componente VARCHAR(45),
modelo_componente VARCHAR(100),
capacidade_componente FLOAT,
fk_maquina INT,
FOREIGN KEY (fk_maquina) REFERENCES Maquina(id_maquina)
);
    
CREATE TABLE Tipo_Registro(
id_tipo_registro INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
descricao_tipo VARCHAR(20)
);

CREATE TABLE Registro(
data_hora DATETIME,
medida FLOAT,
fk_tipo_registro INT,
fk_componente INT,
FOREIGN KEY(fk_componente) REFERENCES Componente(id_componente),
FOREIGN KEY(fk_tipo_registro) REFERENCES Tipo_Registro(id_tipo_registro)
);

INSERT INTO Empresa VALUES 
(null, 'FastSystem', 123456789, 02535412, 1522, 11942563656, 'Endryl', "endryl@gmail.com"),
(null, 'McDonalds', 987654321, 32654845, 365, 11953145796, 'Donald McDonalds', 'dodo@gmail.com');

INSERT INTO Maquina VALUES 
( null, 1, "DESKTOP", "Desktop 1", '', 0, 'felipe.fastsystem@gmail.com', '1234' ),  
( null, 2, "TOTEM", "Totem 1", '', 0, 'endryl.mcdonalds@gmail.com', '1234'),
( null, 2, "DESKTOP", "Desktop 1", '', 0, 'vitoria.mcdonalds@gmail.com', '1234'),
( null, 2, "DESKTOP", "Desktop 2", '', 0, 'jaqueline.mcdonalds@gmail.com', '1234' ),  
( null, 2, "TOTEM", "Totem 2", '', 0, 'ricardo.mcdonalds@gmail.com', '1234'),
( null, 2, "DESKTOP", "Desktop 3", '', 0, 'rafael.mcdonalds@gmail.com', '1234'),
( null, 2, "TOTEM", "Totem 3", '', 0, 'gerson.mcdonalds@gmail.com', '1234'),
( null, 2, "DESKTOP", "Desktop 4", '', 0, 'fernanda.mcdonalds@gmail.com', '1234'), 
( null, 2, "DESKTOP", "Desktop 5", '', 0, 'dudu.fastsystem@gmail.com', '1234' );

INSERT INTO Tipo_Registro VALUES
( null, 'GB' ),
( null, '%' );

INSERT INTO Funcionario VALUES 
(null, 1, 'Endryl', 1, 12345678912, 'endryl@gmail.com', 12345678, '942518747'),
(null, 2, 'Felipe', 1, 98765432112, 'felipe@gmail.com', 12345678, '965321547');

INSERT INTO App VALUES
( null, 'chrome' ),
( null, 'WhatsApp' ),
( null, 'AnyDesk' );

-- Faça esses selects

SELECT nome_app FROM App_Empresa 
JOIN App ON App.id_app = App_Empresa.fk_app
WHERE fk_empresa = 2;

SELECT nome_processo FROM Registro_Processo 
WHERE fk_maquina = 1;

SELECT * FROM Maquina;
SELECT * FROM Registro_Processo;
SELECT nome_processo, is_autorizado FROM Registro_Processo WHERE fk_maquina = 1 GROUP BY nome_processo;
INSERT INTO Registro_Processo VALUES 
( null, 'teste', '2022-11-24 14:37:15', false, 1 );

SELECT nome_app FROM App_Empresa
JOIN App ON App.id_app = App_empresa.fk_app
WHERE fk_empresa = 1;

SELECT id_empresa FROM Empresa JOIN Maquina ON id_empresa = fk_empresa WHERE id_maquina = 3;

SELECT DISTINCT(COUNT(medida)), fk_maquina FROM Registro
INNER JOIN Componente ON Componente.id_componente = Registro.fk_componente
WHERE nome_componente LIKE 'Processador%' AND 
data_hora BETWEEN (SELECT SEC_TO_TIME(TIME_TO_SEC(CURRENT_TIMESTAMP()) - 30)) AND CURRENT_TIMESTAMP() 
AND ((medida * 100) / capacidade_componente) > 80
GROUP BY fk_componente;

SELECT DISTINCT(COUNT(medida)), fk_maquina FROM Registro
INNER JOIN Componente ON Componente.id_componente = Registro.fk_componente
WHERE nome_componente LIKE 'Memória%' AND 
data_hora BETWEEN (SELECT SEC_TO_TIME(TIME_TO_SEC(CURRENT_TIMESTAMP()) - 30)) AND CURRENT_TIMESTAMP() 
AND ((medida * 100) / capacidade_componente) > 80
GROUP BY fk_componente;

SELECT data_hora, ((medida * 100) / capacidade_componente) AS Uso, (100 - ((medida * 100) / capacidade_componente) ) as Livre FROM Registro 
JOIN Componente ON Componente.id_componente = Registro.fk_Componente
JOIN Maquina ON Maquina.id_maquina = Componente.fk_maquina
WHERE fk_maquina = 3 AND nome_componente LIKE 'Memória%' AND
data_hora BETWEEN (SELECT SEC_TO_TIME(TIME_TO_SEC(CURRENT_TIMESTAMP()) - 30)) AND CURRENT_TIMESTAMP();

SELECT * FROM Maquina;

SELECT SEC_TO_TIME(TIME_TO_SEC(CURRENT_TIMESTAMP()) - 30);
SELECT CURRENT_TIMESTAMP();

SELECT * FROM App_Empresa;

SELECT * FROM Maquina
INNER JOIN Empresa ON Empresa.id_empresa = Maquina.fk_empresa
WHERE id_empresa = 2;

SELECT * FROM Registro_Processo;

SELECT id_componente FROM Empresa
		INNER JOIN Maquina ON Empresa.id_empresa = Maquina.fk_empresa
        INNER JOIN Componente ON Maquina.id_maquina = Componente.fk_maquina
        WHERE id_maquina = 1 and nome_componente LIKE 'Processador%';
        
SELECT * FROM Maquina;
SELECT id_componente, nome_componente FROM Empresa
		INNER JOIN Maquina ON Empresa.id_empresa = Maquina.fk_empresa
        INNER JOIN Componente ON Maquina.id_maquina = Componente.fk_maquina
        WHERE id_maquina = 1;
        
select id_empresa, data_hora, medida, capacidade_componente, 
        fabricante_componente, nome_componente, modelo_componente, 
        nome_maquina, sistema_operacional_maquina, tipo_maquina, 
        nome_empresa, (capacidade_componente - medida) as livre
        from Empresa
        INNER JOIN Maquina ON Empresa.id_empresa = Maquina.fk_empresa
        INNER JOIN Componente ON Maquina.id_maquina = Componente.fk_maquina
        INNER JOIN Registro ON Componente.id_componente = Registro.fk_componente
        WHERE nome_componente = 'Disco 2' AND id_empresa = 1
        ORDER BY data_hora ASC LIMIT 1;
        
SELECT nome_componente, modelo_componente, capacidade_componente FROM Empresa
	INNER JOIN Maquina ON Empresa.id_empresa = Maquina.fk_empresa
	INNER JOIN Componente ON Maquina.id_maquina = Componente.fk_maquina
    WHERE id_empresa = 1 AND nome_componente LIKE 'Disco%';
        
SELECT * FROM Maquina;
SELECT * FROM Componente;
SELECT * FROM Registro WHERE fk_componente = 1;
SELECT * FROM Registro_Processo WHERE fk_maquina = 1 GROUP BY nome_processo;

SELECT * FROM Registro_Processo GROUP BY nome_processo;
        
UPDATE Componente SET 
	nome_componente = 'Processadorrrrr', 
	is_ativo = true, 
    fabricante_componente = 'GenuineIntel', 
    modelo_componente = 'Intel(R) Core(TM) i7-10610U CPU @ 1.80GHz', 
    capacidade_componente = 10,
    fk_maquina = 1
		WHERE id_componente = 1 AND nome_componente LIKE 'Processador%';
        
UPDATE Componente SET 
	nome_componente = 'Memóriaaaa', 
	is_ativo = true,
    capacidade_componente = 10,
    fk_maquina = 1
		WHERE id_componente = 2 AND nome_componente LIKE 'Memória%';
        
UPDATE Componente SET 
	nome_componente = 'Disco 1 BEBE', is_ativo = true, modelo_componente = 'SSSTC CA5-8D256-Q79 (Standard disk drives)', capacidade_componente = 256
    WHERE id_componente = 3 AND nome_componente LIKE 'Disco 1%';

SELECT nome_empresa, nome_maquina, nome_componente FROM Empresa
	INNER JOIN Maquina ON Empresa.id_empresa = maquina.fk_empresa
    INNER JOIN Componente ON Maquina.id_maquina = Componente.fk_maquina;
    
SELECT nome_empresa, nome_maquina, nome_componente FROM Empresa
	INNER JOIN Maquina ON Empresa.id_empresa = maquina.fk_empresa
    INNER JOIN Componente ON Maquina.id_maquina = Componente.fk_maquina
		WHERE id_empresa = 1 and id_maquina = 1;
        
-- SELECT DOS RESGISTROS DE DETERMINADA EMPRESA E SUAS MÁQUINAS
SELECT id_empresa, nome_maquina, data_hora, medida FROM Empresa
	INNER JOIN Maquina ON Empresa.id_empresa = maquina.fk_empresa
    INNER JOIN Componente ON Maquina.id_maquina = Componente.fk_maquina
    INNER JOIN Registro ON Registro.fk_componente = Componente.id_componente
		WHERE id_empresa = 2;
        
SELECT nome_empresa, id_maquina FROM Empresa
	INNER JOIN Maquina ON Empresa.id_empresa = maquina.fk_empresa;

/*
TESTES
SELECT * FROM Componente;
TRUNCATE Componente;
DROP TABLE Componente;

UPDATE Componente SET nome_componente = "", is_ativo = true, fabricante_componente = "", modelo_componente = "", capacidade_componente = 0 WHERE id_componenete = 1;

Processador
INSERT INTO Componente VALUES
( null, 'Processador', true, "GenuineIntel", 'Intel(R) Core(TM) i3-4005U CPU @ 1.70GHz', 100 );
INSERT INTO Componente VALUES
( null, 'Processador', true, "GenuineIntel", 'Intel(R) Core(TM) i3-4005U CPU @ 1.70GHz', 100 );
Memória
INSERT INTO Componente (id_componente, nome_componente, is_ativo, capacidade_componente) VALUES
( null, 'Memória', true, 100 );
Disco
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

SELECTS

SELECT nome_empresa, id_maquina FROM Empresa
	INNER JOIN Maquina ON Empresa.id_empresa = maquina.fk_empresa;

SELECT nome_empresa, nome_maquina, nome_componente FROM Empresa
	INNER JOIN Maquina ON Empresa.id_empresa = maquina.fk_empresa
	INNER JOIN Componente_Maquina ON Maquina.id_maquina = Componente_Maquina.fk_maquina
    INNER JOIN Componente ON Componente.id_componente = Componente_Maquina.fk_componente
		WHERE id_empresa = 1 and id_maquina = 1;
        
SELECT * FROM Componente;
        
SELECT nome_maquina, id_maquina, fk_componente FROM Componente
	INNER JOIN Componente_Maquina ON Componente_Maquina.fk_componente = Componente.id_componente
	INNER JOIN Maquina ON Maquina.id_maquina = Componente_Maquina.fk_maquina
		WHERE id_maquina = 1;
        
SELECT nome_maquina, id_maquina, fk_componente FROM Componente_Maquina
	INNER JOIN Maquina ON Maquina.id_maquina = Componente_Maquina.fk_maquina
		WHERE id_maquina = 1;
        
SELECT id_empresa, id_maquina FROM Maquina
	INNER JOIN empresa ON empresa.id_empresa = maquina.fk_empresa
		WHERE nome_maquina = 'Desktop 1' and email_empresa = 'endryl@gmail.com' and senha_empresa = 12345678;*/