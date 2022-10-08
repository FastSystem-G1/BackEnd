package com.fastsystem.login.fastsystem;

import com.github.britooo.looca.api.core.Looca;
import javax.swing.JOptionPane;

public class InformacoesMaquina {
    Looca looca = new Looca();
    
    String informacoesMemoria = String.format(
            "\nMemória"
            + "\nTotal: %d GB"
            + "\nEm uso: %d GB"
            + "\nDisponivel: %d GB",
            (looca.getMemoria().getTotal() / 1000000000),
            (looca.getMemoria().getEmUso() / 1000000000),
            (looca.getMemoria().getDisponivel() / 1000000000)
    );
    
    String informacoesSistema = String.format(
            "\nSistema"
            + "\nSistema Operacional: %s"
            + "\nTempo de Atividade em segundos: %d", 
            looca.getSistema().getSistemaOperacional(),
            looca.getSistema().getTempoDeAtividade()
    );
    
    String informacoesProcessador = String.format(
            "\nProcessador"
            + "\nNome do processador: %s"
            + "\nNome do Fabricante: %s"
            + "\nDisponivel: %.2f"
            + "\nEm uso: %.2f", 
            looca.getProcessador().getNome(),
            looca.getProcessador().getFabricante(),
            (100 - looca.getProcessador().getUso()),
            looca.getProcessador().getUso()
    );
    
    String informacoesDisco = String.format(
            "\nDiscos"
            + "\nQuantidade de discos: %d"
            + "\nTamanho total disponivel: %d GB"
            + "\nLista dos discos: %s",
            looca.getGrupoDeDiscos().getQuantidadeDeDiscos(),
            (looca.getGrupoDeDiscos().getTamanhoTotal() / 1000000000),
            looca.getGrupoDeDiscos().getDiscos()
    );
    
    String informacoesProcessos = String.format(
            "\nProcessos"
            +"\nTotal de Processos: %d"
            +"\nLista dos Processos Ativos: %s",
            looca.getGrupoDeProcessos().getTotalProcessos(),
            looca.getGrupoDeProcessos().getProcessos()
    );
    
    public void informacoes() {
        System.out.println(
                this.informacoesSistema
                +"\n"+
                this.informacoesMemoria
                +"\n"+
                this.informacoesProcessador
                +"\n"+
                this.informacoesDisco
        );
    }
}
