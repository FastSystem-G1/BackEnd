package com.fastsystem.login.fastsystem;

import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

public class InformacoesMaquina {

    Looca looca = new Looca();
    Conexao con = new Conexao();
    JdbcTemplate banco = con.getConnection();
    EmpresaMaquina maquinaInfo;
    
    LocalDateTime dataAtual = LocalDateTime.now();
    DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String dataFormatada = dataAtual.format(formatoData);
    Long tempoAtividade;

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
                    + "INNER JOIN Componente ON Maquina.id_maquina = Componente.fk_maquina\n"
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
        tempoAtividade = looca.getSistema().getTempoDeAtividade();
        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            @Override
            public void run() {
                tempoAtividade = looca.getSistema().getTempoDeAtividade();
                banco.update(
                        "UPDATE Maquina SET "
                        + "sistema_operacional_maquina = '" + sistemaOperacional + "', "
                        + "tempo_atividade_maquina = " + tempoAtividade
                        + " WHERE id_maquina = " + idMaquina + ";"
                );
            }
        };
        timer.scheduleAtFixedRate(tarefa, 0, 5000);
    }

    public void inserirInformacoesProcessador(Integer idMaquina) {
        String nome = "Processador";
        Boolean isAtivo = true;
        String fabricante = looca.getProcessador().getFabricante();
        String modelo = looca.getProcessador().getNome();
        Integer capacidade = 100;
        //Calculo para descobrir quanto está disponivel: (100 - looca.getProcessador().getUso());

        Integer idComponenteBanco = componenteExiste(idMaquina, nome);
        Integer idComponente;

        if (idComponenteBanco != 0) {
            banco.update(
                    "UPDATE Componente SET "
                    + "nome_componente = '" + nome + "', "
                    + "is_ativo = " + isAtivo + ", "
                    + "fabricante_componente = '" + fabricante + "', "
                    + "modelo_componente = '" + modelo + "', "
                    + "capacidade_componente = " + capacidade + ", "
                    + "fk_maquina = " + idMaquina + " "
                    + "WHERE id_componente = " + idComponenteBanco + " "
                    + "AND nome_componente LIKE '" + nome + "%';"
            );
            idComponente = idComponenteBanco;
            System.out.println("Processador atualizado");
        } else {
            //  Inserindo no campo o processador da máquina.
            banco.update(
                    "INSERT INTO Componente VALUES"
                    + "( null, '" + nome + "', " + isAtivo + ", '" + fabricante + "', '" + modelo + "', " + capacidade + ", " + idMaquina + ");"
            );
            //  Puxando do banco a ID do componente que acabou de ser criado.
            maquinaInfo = banco.queryForObject(
                    "SELECT id_componente FROM Componente ORDER BY id_componente DESC LIMIT 1;", new BeanPropertyRowMapper<>(EmpresaMaquina.class)
            );
            idComponente = maquinaInfo.getIdComponente();
            System.out.println("Processador cadastrado");
        }
        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            @Override
            public void run() {
                dataAtual = LocalDateTime.now();
                banco.update(
                        "INSERT INTO Registro VALUES"
                        + "('" + dataFormatada + "', " + looca.getProcessador().getUso() + ", 2, " + idComponente + ");"
                );
            }
        };
        timer.scheduleAtFixedRate(tarefa, 0, 5000);
    }

    public void inserirInformacoesMemoria(Integer idMaquina) {
        String nome = "Memória";
        Boolean isAtivo = true;
        Long capacidade = looca.getMemoria().getTotal() / 1000000000;

        Integer idComponenteBanco = componenteExiste(idMaquina, nome);
        Integer idComponente;

        if (idComponenteBanco != 0) {
            banco.update(
                    "UPDATE Componente SET "
                    + "nome_componente = '" + nome + "', "
                    + "is_ativo = " + isAtivo + ", "
                    + "capacidade_componente = " + capacidade + ", "
                    + "fk_maquina = " + idMaquina + " "
                    + "WHERE id_componente = " + idComponenteBanco + " AND nome_componente LIKE '" + nome + "%';"
            );
            idComponente = idComponenteBanco;
            System.out.println("Memória atualizada");
        } else {
            banco.update(
                    "INSERT INTO Componente (id_componente, nome_componente, is_ativo, capacidade_componente, fk_maquina) VALUES "
                    + "( null, '" + nome + "', " + isAtivo + ", " + capacidade + ", " + idMaquina + ");"
            );
            maquinaInfo = banco.queryForObject(
                    "SELECT id_componente FROM Componente ORDER BY id_componente DESC LIMIT 1;", new BeanPropertyRowMapper<>(EmpresaMaquina.class)
            );
            idComponente = maquinaInfo.getIdComponente();
            // Tipo_Registro = 1 == GB
            // Tipo_Registro = 2 == %
            System.out.println("Memória cadastrada");
        }
        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            @Override
            public void run() {
                dataAtual = LocalDateTime.now();
                banco.update(
                        "INSERT INTO Registro VALUES"
                        + "('" + dataFormatada + "', " + (looca.getMemoria().getEmUso() / 1000000000) + ", 1, " + idComponente + ");"
                );
            }
        };
        timer.scheduleAtFixedRate(tarefa, 0, 5000);
    }

    public void inserirInformacoesDisco(Integer idMaquina) {
        Boolean isAtivo = true;
        Integer quantidade = looca.getGrupoDeDiscos().getQuantidadeDeDiscos();

        for (int i = 1; i <= quantidade; i++) {
            String nome = "Disco " + i;
            String modelo = looca.getGrupoDeDiscos().getDiscos().get(i - 1).getModelo();
            Long capacidade = looca.getGrupoDeDiscos().getVolumes().get(i - 1).getTotal() / 1000000000;
            Long uso = looca.getGrupoDeDiscos().getVolumes().get(i - 1).getDisponivel() / 1000000000;
            
            if (capacidade == 0) {
                capacidade = looca.getGrupoDeDiscos().getDiscos().get(i).getTamanho();
            }

            Integer idComponenteBanco = componenteExiste(idMaquina, nome);
            Integer idComponente;

            if (idComponenteBanco != 0) {
                banco.update(
                        "UPDATE Componente SET "
                        + "nome_componente = '" + nome + "', "
                        + "is_ativo = " + isAtivo + ", "
                        + "modelo_componente = '" + modelo + "', "
                        + "capacidade_componente = " + capacidade + ", "
                        + "fk_maquina = " + idMaquina + " "
                        + "WHERE id_componente = " + idComponenteBanco + " "
                        + "AND nome_componente LIKE '" + nome + "%';"
                );
                idComponente = idComponenteBanco;
            } else {
                banco.update(
                        "INSERT INTO Componente (id_componente, nome_componente, is_ativo, modelo_componente, capacidade_componente, fk_maquina) VALUES "
                        + "( null, '" + nome + "', " + isAtivo + ", '" + modelo + "', " + capacidade + ", " + idMaquina + ");"
                );
                maquinaInfo = banco.queryForObject(
                        "SELECT id_componente FROM Componente ORDER BY id_componente DESC LIMIT 1;", new BeanPropertyRowMapper<>(EmpresaMaquina.class)
                );
                idComponente = maquinaInfo.getIdComponente();
            }
            Timer timer = new Timer();
            TimerTask tarefa = new TimerTask() {
                @Override
                public void run() {
                    dataAtual = LocalDateTime.now();
                    banco.update(
                            "INSERT INTO Registro VALUES "
                            + "('" + dataFormatada + "', " + uso + ", 1, " + idComponente + ");"
                    );
                }
            };
            timer.scheduleAtFixedRate(tarefa, 0, 5000);
        }
        System.out.println(quantidade + " disco(s) cadastrado(s).");
    }
}
