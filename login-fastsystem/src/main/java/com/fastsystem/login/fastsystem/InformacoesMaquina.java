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
    Conexao con2 = new Conexao();
    JdbcTemplate banco = con.getConnection();
    JdbcTemplate banco2 = con2.getConnectionAzure();
    EmpresaMaquina maquinaInfo;
    
    LocalDateTime dataAtual = LocalDateTime.now();
    DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String dataFormatada = dataAtual.format(formatoData);
    Long tempoAtividade;

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
        EmpresaMaquina select;
        select = banco.queryForObject(
                "SELECT id_empresa FROM Empresa JOIN Maquina ON id_empresa=fk_empresa WHERE id_maquina="+idMaquina+";", 
                new BeanPropertyRowMapper<>(EmpresaMaquina.class)
        );
        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            @Override
            public void run() {
                inserirInformacoesProcesso(idMaquina, select.getIdEmpresa());
            }
        };
        timer.scheduleAtFixedRate(tarefa, 0, 30000);
        
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
        //Calculo para descobrir quanto est?? disponivel: (100 - looca.getProcessador().getUso());

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
            //  Inserindo no campo o processador da m??quina.
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
                dataFormatada = dataAtual.format(formatoData);
                banco.update(
                        "INSERT INTO Registro VALUES"
                        + "('" + dataFormatada + "', " + looca.getProcessador().getUso() + ", 2, " + idComponente + ");"
                );
            }
        };
        timer.scheduleAtFixedRate(tarefa, 0, 5000);
    }

    public void inserirInformacoesMemoria(Integer idMaquina) {
        String nome = "Mem??ria";
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
            System.out.println("Mem??ria atualizada");
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
            System.out.println("Mem??ria cadastrada");
        }
        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            Long emUso;
            @Override
            public void run() {
                dataAtual = LocalDateTime.now();
                dataFormatada = dataAtual.format(formatoData);
                emUso = looca.getMemoria().getEmUso() / 1000000000;
                banco.update(
                        "INSERT INTO Registro VALUES"
                        + "('" + dataFormatada + "', " + emUso + ", 1, " + idComponente + ");"
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
                capacidade = looca.getGrupoDeDiscos().getDiscos().get(i - 1).getTamanho() / 1000000000;
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
                    dataFormatada = dataAtual.format(formatoData);
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
    
    public void inserirInformacoesProcesso(Integer idMaquina, Integer idEmpresa) {
        Integer quantidade = looca.getGrupoDeProcessos().getTotalProcessos();
        dataAtual = LocalDateTime.now();
        dataFormatada = dataAtual.format(formatoData);
        Integer quantidadeProcessos = 0;
        List apps = banco.queryForList(
            "SELECT nome_app FROM App_Empresa\n" +
            "JOIN App ON id_app = fk_app\n" +
            "WHERE fk_empresa = "+idEmpresa+";"  
        );

        try {
            banco.update(
                "DELETE FROM Registro_Processo WHERE fk_maquina = " + idMaquina + ";"
            );
        } catch (Exception e) {
            System.out.println("A m??quina n??o possu?? processos salvos.");
        }
        
        try {
            for (int i = 0; i < quantidade; i++) {
                String nome = looca.getGrupoDeProcessos().getProcessos().get(i).getNome();
                Boolean is_autorizado = true;
                for (int j = 0; j < apps.size(); j ++) {
                    String nomeLista = apps.get(j).toString();
                    String nomeFormatado = nomeLista.substring(10, nomeLista.length()-1);
                    if (nomeFormatado.equals(nome)) {
                        is_autorizado = false;
                    }
                }
                banco.update(
                        "INSERT INTO Registro_Processo (id_registro_processo, nome_processo, data_hora, is_autorizado, fk_maquina) VALUES "
                        + "( null, '" + nome + "', '" + dataFormatada + "', " + is_autorizado + "," + idMaquina + ");"
                );
                quantidadeProcessos++;
            }
            System.out.println(quantidadeProcessos+" de Processos cadastrados com sucesso.");
        } catch(Exception e) {
            System.out.println(
                    "\nQuantidade de processos salvos no banco at?? a exception: "+quantidadeProcessos+"."+
                    "\nQuantidade de processos inicial ao todo: "+quantidade+".\n"
            );
        }
    }
    
    
    // BANCO DA AZURE!!!
    
    public Integer componenteExisteAzure(Integer idMaquina, String nomeComponente) {
        try {
            EmpresaMaquina componente;
            componente = banco2.queryForObject(
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

    public void inserirInformacoesBancoAzure(Integer idMaquina) {
        EmpresaMaquina select;
        select = banco2.queryForObject(
                "SELECT id_empresa FROM Empresa JOIN Maquina ON id_empresa=fk_empresa WHERE id_maquina="+idMaquina+";", 
                new BeanPropertyRowMapper<>(EmpresaMaquina.class)
        );
        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            @Override
            public void run() {
                inserirInformacoesProcessoAzure(idMaquina, select.getIdEmpresa());
            }
        };
        timer.scheduleAtFixedRate(tarefa, 0, 30000);
        
        inserirInformacoesSistemaAzure(idMaquina);
        inserirInformacoesProcessadorAzure(idMaquina);
        inserirInformacoesMemoriaAzure(idMaquina);
        inserirInformacoesDiscoAzure(idMaquina);
        JOptionPane.showMessageDialog(
                null,
                "Login efetuado com sucesso!",
                "Login autorizado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void inserirInformacoesSistemaAzure(Integer idMaquina) {
        String sistemaOperacional = looca.getSistema().getSistemaOperacional();
        tempoAtividade = looca.getSistema().getTempoDeAtividade();
        banco2.update(
                "UPDATE Maquina SET "
                + "sistema_operacional_maquina = '" + sistemaOperacional + "', "
                + "tempo_atividade_maquina = " + tempoAtividade
                + " WHERE id_maquina = " + idMaquina + ";"
        );
    }

    public void inserirInformacoesProcessadorAzure(Integer idMaquina) {
        String nome = "Processador";
        Integer isAtivo = 1;
        String fabricante = looca.getProcessador().getFabricante();
        String modelo = looca.getProcessador().getNome();
        Integer capacidade = 100;
        //Calculo para descobrir quanto est?? disponivel: (100 - looca.getProcessador().getUso());

        Integer idComponenteBanco = componenteExisteAzure(idMaquina, nome);
        Integer idComponente;

        if (idComponenteBanco != 0) {
            banco2.update(
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
            //  Inserindo no campo o processador da m??quina.
            banco2.update(
                    "INSERT INTO Componente VALUES"
                    + "('" + nome + "', " + isAtivo + ", '" + fabricante + "', '" + modelo + "', " + capacidade + ", " + idMaquina + ");"
            );
            //  Puxando do banco a ID do componente que acabou de ser criado.
            maquinaInfo = banco2.queryForObject(
                    "SELECT id_componente FROM (SELECT TOP 1* FROM Componente ORDER BY id_componente DESC) A ORDER BY id_componente ASC;", new BeanPropertyRowMapper<>(EmpresaMaquina.class)
            );
            idComponente = maquinaInfo.getIdComponente();
            System.out.println("Processador cadastrado");
        }
        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            @Override
            public void run() {
                dataAtual = LocalDateTime.now();
                dataFormatada = dataAtual.format(formatoData);
                banco2.update(
                        "INSERT INTO Registro VALUES"
                        + "('" + dataFormatada + "', " + looca.getProcessador().getUso() + ", 2, " + idComponente + ");"
                );
            }
        };
        timer.scheduleAtFixedRate(tarefa, 0, 5000);
    }

    public void inserirInformacoesMemoriaAzure(Integer idMaquina) {
        String nome = "Mem??ria";
        Integer isAtivo = 1;
        Long capacidade = looca.getMemoria().getTotal() / 1000000000;

        Integer idComponenteBanco = componenteExisteAzure(idMaquina, nome);
        Integer idComponente;

        if (idComponenteBanco != 0) {
            banco2.update(
                    "UPDATE Componente SET "
                    + "nome_componente = '" + nome + "', "
                    + "is_ativo = " + isAtivo + ", "
                    + "capacidade_componente = " + capacidade + ", "
                    + "fk_maquina = " + idMaquina + " "
                    + "WHERE id_componente = " + idComponenteBanco + " AND nome_componente LIKE '" + nome + "%';"
            );
            idComponente = idComponenteBanco;
            System.out.println("Mem??ria atualizada");
        } else {
            banco2.update(
                    "INSERT INTO Componente (nome_componente, is_ativo, capacidade_componente, fk_maquina) VALUES "
                    + "('" + nome + "', " + isAtivo + ", " + capacidade + ", " + idMaquina + ");"
            );
            maquinaInfo = banco2.queryForObject(
                    "SELECT id_componente FROM (SELECT TOP 1* FROM Componente ORDER BY id_componente DESC) A ORDER BY id_componente ASC;", new BeanPropertyRowMapper<>(EmpresaMaquina.class)
            );
            idComponente = maquinaInfo.getIdComponente();
            // Tipo_Registro = 1 == GB
            // Tipo_Registro = 2 == %
            System.out.println("Mem??ria cadastrada");
        }
        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            Long emUso;
            @Override
            public void run() {
                dataAtual = LocalDateTime.now();
                dataFormatada = dataAtual.format(formatoData);
                emUso = looca.getMemoria().getEmUso() / 1000000000;
                banco2.update(
                        "INSERT INTO Registro VALUES"
                        + "('" + dataFormatada + "', " + emUso + ", 1, " + idComponente + ");"
                );
            }
        };
        timer.scheduleAtFixedRate(tarefa, 0, 5000);
    }

    public void inserirInformacoesDiscoAzure(Integer idMaquina) {
        Integer isAtivo = 1;
        Integer quantidade = looca.getGrupoDeDiscos().getQuantidadeDeDiscos();

        for (int i = 1; i <= quantidade; i++) {
            String nome = "Disco " + i;
            String modelo = looca.getGrupoDeDiscos().getDiscos().get(i - 1).getModelo();
            Long capacidade = looca.getGrupoDeDiscos().getVolumes().get(i - 1).getTotal() / 1000000000;
            Long uso = looca.getGrupoDeDiscos().getVolumes().get(i - 1).getDisponivel() / 1000000000;
            
            if (capacidade == 0) {
                capacidade = looca.getGrupoDeDiscos().getDiscos().get(i - 1).getTamanho() / 1000000000;
            }

            Integer idComponenteBanco = componenteExisteAzure(idMaquina, nome);
            Integer idComponente;

            if (idComponenteBanco != 0) {
                banco2.update(
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
                banco2.update(
                        "INSERT INTO Componente (nome_componente, is_ativo, modelo_componente, capacidade_componente, fk_maquina) VALUES "
                        + "('" + nome + "', " + isAtivo + ", '" + modelo + "', " + capacidade + ", " + idMaquina + ");"
                );
                maquinaInfo = banco2.queryForObject(
                        "SELECT id_componente FROM (SELECT TOP 1* FROM Componente ORDER BY id_componente DESC) A ORDER BY id_componente ASC;", new BeanPropertyRowMapper<>(EmpresaMaquina.class)
                );
                idComponente = maquinaInfo.getIdComponente();
            }
            Timer timer = new Timer();
            TimerTask tarefa = new TimerTask() {
                @Override
                public void run() {
                    dataAtual = LocalDateTime.now();
                    dataFormatada = dataAtual.format(formatoData);
                    banco2.update(
                            "INSERT INTO Registro VALUES "
                            + "('" + dataFormatada + "', " + uso + ", 1, " + idComponente + ");"
                    );
                }
            };
            timer.scheduleAtFixedRate(tarefa, 0, 5000);
        }
        System.out.println(quantidade + " disco(s) cadastrado(s).");
    }
    
    public void inserirInformacoesProcessoAzure(Integer idMaquina, Integer idEmpresa) {
        Integer quantidade = looca.getGrupoDeProcessos().getTotalProcessos();
        dataAtual = LocalDateTime.now();
        dataFormatada = dataAtual.format(formatoData);
        Integer quantidadeProcessos = 0;
        List apps = banco2.queryForList(
            "SELECT nome_app FROM App_Empresa\n" +
            "JOIN App ON id_app = fk_app\n" +
            "WHERE fk_empresa = "+idEmpresa+";"  
        );

        try {
            banco2.update(
                "DELETE FROM Registro_Processo WHERE fk_maquina = " + idMaquina + ";"
            );
        } catch (Exception e) {
            System.out.println("A m??quina n??o possu?? processos salvos.");
        }
        
        try {
            for (int i = 0; i < quantidade; i++) {
                String nome = looca.getGrupoDeProcessos().getProcessos().get(i).getNome();
                Integer is_autorizado = 1;
                for (int j = 0; j < apps.size(); j ++) {
                    String nomeLista = apps.get(j).toString();
                    String nomeFormatado = nomeLista.substring(10, nomeLista.length()-1);
                    if (nomeFormatado.equals(nome)) {
                        is_autorizado = 0;
                    }
                }
                banco2.update(
                        "INSERT INTO Registro_Processo (nome_processo, data_hora, is_autorizado, fk_maquina) VALUES "
                        + "('" + nome + "', '" + dataFormatada + "', " + is_autorizado + "," + idMaquina + ");"
                );
                quantidadeProcessos++;
            }
            System.out.println(quantidadeProcessos+" de Processos cadastrados com sucesso.");
        } catch(Exception e) {
            System.out.println(
                    "\nQuantidade de processos salvos ne Azure at?? a exception: "+quantidadeProcessos+"."+
                    "\nQuantidade de processos inicial ao todo: "+quantidade+".\n"
            );
        }
    }
    
}
