CREATE DATABASE FastSystem;
USE FastSystem;

-- DROP DATABASE FastSystem;

CREATE TABLE Empresa(
id_empresa INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
nome_empresa VARCHAR(100),
cnpj_empresa VARCHAR (14),
cep_empresa VARCHAR(11),
numero_empresa INT,
telefone_empresa VARCHAR(14),
representante VARCHAR(100),
email_empresa VARCHAR(50)
)AUTO_INCREMENT = 0;

CREATE TABLE Funcionario (
id_funcionario INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
fk_empresa INT,
nome_funcionario VARCHAR(100),
is_admin BOOLEAN,
cpf_funcionario VARCHAR(11),
email_funcionario VARCHAR(50),
senha_funcionario VARCHAR(25),
telefone_funcionario VARCHAR(14),
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
is_ativo BOOLEAN,
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
(null, 'Fast Food 1', 123456789, 02535412, 1522, 11942563656, 'Endryl', "admin.fastfood1@gmail.com"),
(null, 'Fast Food 2', 987654321, 32654845, 365, 11953145796, 'Donald McDonalds', 'admin.fastfood2@gmail.com');

INSERT INTO Funcionario VALUES 
(null, 1, 'Endryl', 1, 12345678912, 'endryl@gmail.com', 12345678, '942518747'),
(null, 1, 'Alex', 0, 12345678912, 'alex@gmail.com', 12345678, '942518747'),
(null, 2, 'Felipe', 1, 98765432112, 'felipe@gmail.com', 12345678, '965321547'),
(null, 2, 'Gerson', 0, '12345678912', 'gerson@gmail.com', 12345678, '942518747');

INSERT INTO Maquina VALUES 
(null, 1, 'DESKTOP', 'Admin PC', '', 0, 'admin.fastfood1@gmail.com', '1234' ),
(null, 2, 'TOTEM', 'Caixa 1', '', 0, 'caixa1.fastfood2@gmail.com', '1234'),
(null, 2, 'DESKTOP', 'Admin PC', '', 0, 'admin.fastfood2@gmail.com', '1234'),
(null, 2, 'DESKTOP', 'Caixa 2', '', 0, 'caixa2.fastfood2@gmail.com', '1234' ),
(null, 2, 'TOTEM', 'Caixa 3', '', 0, 'caixa3.fastfood2@gmail.com', '1234'),
(null, 2, 'DESKTOP', 'Totem 1', '', 0, 'totem1.fastfood2s@gmail.com', '1234'),
(null, 2, 'TOTEM', 'Totem 2', '', 0, 'totem2.fastfood2@gmail.com', '1234'),
(null, 2, 'DESKTOP', 'Totem 3', '', 0, 'totem3.fastfood2@gmail.com', '1234'), 
(null, 2, 'DESKTOP', 'Caixa 4', '', 0, 'caixa4.fastfood2@gmail.com', '1234' ); 

INSERT INTO App VALUES
(null, 'chrome'),
(null, 'WhatsApp'),
(null, 'AnyDesk'),
(null, 'Code');

INSERT INTO Tipo_Registro VALUES
(null, 'GB'),
(null, '%');