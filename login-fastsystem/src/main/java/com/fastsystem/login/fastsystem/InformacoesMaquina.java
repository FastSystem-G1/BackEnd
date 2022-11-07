package com.fastsystem.login.fastsystem;

import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;

public class InformacoesMaquina {

    Looca looca = new Looca();
    Conexao con = new Conexao();
    JdbcTemplate banco = con.getConnection();
    EmpresaMaquina maquinaInfo;
    LocalDateTime dataAtual = LocalDateTime.now();
    DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String dataFormatada = dataAtual.format(formatoData);

    /*public void teste() {
        System.out.println(looca.getGrupoDeProcessos().getProcessos());
        /*for ( int i = 0; i < looca.getGrupoDeProcessos().getTotalProcessos(); i++ ) {
            if ( looca.getGrupoDeProcessos().getProcessos().get(i).getNome().equalsIgnoreCase("chrome") ) {
                System.out.println(String.format(
                        "Nome do processo: %s", 
                        looca.getGrupoDeProcessos().getProcessos().get(i).getNome()
                    )
                );
            }
        }
    }*/
    public Integer componenteExiste(Integer idMaquina, String nomeComponente) {
        try {
            EmpresaMaquina componente;
            componente = banco.queryForObject(
                    "SELECT id_componente FROM Empresa\n"
                    + "INNER JOIN Maquina ON Empresa.id_empresa = maquina.fk_empresa\n"
                    + "INNER JOIN Componente_Maquina ON Maquina.id_maquina = Componente_Maquina.fk_maquina\n"
                    + "INNER JOIN Componente ON Componente.id_componente = Componente_Maquina.fk_componente\n"
                    + "WHERE id_maquina = " + idMaquina + " and nome_componente LIKE '" + nomeComponente + "%';",
                    new BeanPropertyRowMapper<>(EmpresaMaquina.class)
            );
            return componente.getIdComponente();
        } catch (Exception e) {
            return 0;
        }
    }

    public void inserirInformacoesBanco(Integer idMaquina) {
        inserirInformacoesSistema(idMaquina);
        inserirInformacoesProcessador(idMaquina);
        inserirInformacoesMemoria(idMaquina);
        inserirInformacoesDisco(idMaquina);
        JOptionPane.showMessageDialog(
                null,
                "Login efetuado com sucesso!",
                "Login autorizado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void inserirInformacoesSistema(Integer idMaquina) {
        String sistemaOperacional = looca.getSistema().getSistemaOperacional();
        Long tempoAtividade = looca.getSistema().getTempoDeAtividade();
        banco.update(
                "UPDATE Maquina SET "
                + "sistema_operacional_maquina = '" + sistemaOperacional + "', "
                + "tempo_atividade_maquina = " + tempoAtividade
                + " WHERE id_maquina = " + idMaquina + ";"
        );
    }

    public void inserirInformacoesProcessador(Integer idMaquina) {
        String nome = "Processador";
        Boolean isAtivo = true;
        String fabricante = looca.getProcessador().getFabricante();
        String modelo = looca.getProcessador().getNome();
        Integer capacidade = 100;
        //Calculo para descobrir quanto está disponivel: (100 - looca.getProcessador().getUso());

        Integer idComponenteBanco = componenteExiste(idMaquina, nome);
        if (idComponenteBanco != 0) {
            banco.update(
                    "UPDATE Componente SET "
                    + "nome_componente = '" + nome + "', "
                    + "is_ativo = " + isAtivo + ", "
                    + "fabricante_componente = '" + fabricante + "', "
                    + "modelo_componente = '" + modelo + "', "
                    + "capacidade_componente = " + capacidade + " "
                    + "WHERE id_componente = " + idComponenteBanco + " "
                    + "AND nome_componente LIKE '" + nome + "%';"
            );
            banco.update(
                    "INSERT INTO Registro VALUES"
                    + "( " + idComponenteBanco + ", " + idMaquina + ", '" + dataFormatada + "', " + looca.getProcessador().getUso() + ", " + 2 + " );"
            );
            System.out.println("Processador atualizado");
        } else {
            //  Inserindo no campo o processador da máquina.
            banco.update(
                    "INSERT INTO Componente VALUES"
                    + "( null, '" + nome + "', " + isAtivo + ", '" + fabricante + "', '" + modelo + "', " + capacidade + ");"
            );
            //  Puxando do banco a ID do componente que acabou de ser criado.
            maquinaInfo = banco.queryForObject(
                    "SELECT id_componente FROM Componente ORDER BY id_componente DESC LIMIT 1;", new BeanPropertyRowMapper<>(EmpresaMaquina.class)
            );
            Integer idComponente = maquinaInfo.getIdComponente();
            // Tipo_Registro = 1 == GB
            // Tipo_Registro = 2 == %
            banco.update(
                    "INSERT INTO Registro VALUES"
                    + "( " + idComponente + ", " + idMaquina + ", '" + dataFormatada + "', " + looca.getProcessador().getUso() + ", " + 2 + " );"
            );
            //  Inserindo na tabela associativa o ID do componente e o ID da máquina que este componente pertence.
            banco.update(
                    "INSERT INTO Componente_Maquina VALUES"
                    + "( " + maquinaInfo.getIdComponente() + ", " + idMaquina + " );"
            );
            System.out.println("Processador cadastrado");
        }
    }

    public void inserirInformacoesMemoria(Integer idMaquina) {
        String nome = "Memória";
        Boolean isAtivo = true;
        Long capacidade = looca.getMemoria().getTotal() / 1000000000;

        Integer idComponenteBanco = componenteExiste(idMaquina, nome);
        if (idComponenteBanco != 0) {
            banco.update(
                    "UPDATE Componente SET "
                    + "nome_componente = '" + nome + "', "
                    + "is_ativo = " + isAtivo + ", "
                    + "capacidade_componente = " + capacidade + " "
                    + "WHERE id_componente = " + idComponenteBanco + " AND nome_componente LIKE '" + nome + "%';"
            );
            banco.update(
                    "INSERT INTO Registro VALUES"
                    + "( " + idComponenteBanco + ", " + idMaquina + ", '" + dataFormatada + "', " + (looca.getMemoria().getEmUso() / 1000000000) + ", " + 1 + " );"
            );
            System.out.println("Memória atualizada");
        } else {
            banco.update(
                    "INSERT INTO Componente (id_componente, nome_componente, is_ativo, capacidade_componente) VALUES "
                    + "( null, '" + nome + "', " + isAtivo + ", " + capacidade + ");"
            );
            maquinaInfo = banco.queryForObject(
                    "SELECT id_componente FROM Componente ORDER BY id_componente DESC LIMIT 1;", new BeanPropertyRowMapper<>(EmpresaMaquina.class)
            );
            Integer idComponente = maquinaInfo.getIdComponente();
            // Tipo_Registro = 1 == GB
            // Tipo_Registro = 2 == %
            banco.update(
                    "INSERT INTO Registro VALUES"
                    + "( " + idComponente + ", " + idMaquina + ", '" + dataFormatada + "', " + (looca.getMemoria().getEmUso() / 1000000000) + ", " + 1 + " );"
            );
            banco.update(
                    "INSERT INTO Componente_Maquina VALUES"
                    + "( " + maquinaInfo.getIdComponente() + ", " + idMaquina + " );"
            );
            System.out.println("Memória cadastrada");
        }
    }

    public void inserirInformacoesDisco(Integer idMaquina) {
        Boolean isAtivo = true;
        Integer quantidade = looca.getGrupoDeDiscos().getQuantidadeDeDiscos();

        for (int i = 1; i <= quantidade; i++) {
            String nome = "Disco " + i;
            String modelo = looca.getGrupoDeDiscos().getDiscos().get(i - 1).getModelo();
            Long capacidade = looca.getGrupoDeDiscos().getDiscos().get(i - 1).getTamanho() / 1000000000;

            Integer idComponenteBanco = componenteExiste(idMaquina, nome);
            if (idComponenteBanco != 0) {
                banco.update(
                        "UPDATE Componente SET nome_componente = '"+nome+"', is_ativo = "+isAtivo+", modelo_componente = '"+modelo+"', capacidade_componente = "+capacidade+" "
                        + "WHERE id_componente = "+idComponenteBanco+" AND nome_componente LIKE '"+nome+"%';"
                );
            } else {
                banco.update(
                        "INSERT INTO Componente (id_componente, nome_componente, is_ativo, modelo_componente, capacidade_componente) VALUES "
                        + "( null, '" + nome + "', " + isAtivo + ", '" + modelo + "', " + capacidade + ");"
                );
                maquinaInfo = banco.queryForObject(
                        "SELECT id_componente FROM Componente ORDER BY id_componente DESC LIMIT 1;", new BeanPropertyRowMapper<>(EmpresaMaquina.class)
                );
                Integer idComponente = maquinaInfo.getIdComponente();

                banco.update(
                        "INSERT INTO Componente_Maquina VALUES"
                        + "( " + idComponente + ", " + idMaquina + " );"
                );
            }
        }
        System.out.println(quantidade + " disco(s) cadastrado(s).");
    }
}
