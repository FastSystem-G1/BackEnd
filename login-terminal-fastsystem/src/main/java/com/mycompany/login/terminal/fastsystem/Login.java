/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.login.terminal.fastsystem;

import static java.lang.Thread.sleep;
import java.util.List;
import java.util.Scanner;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
/**
 *
 * @author fe-go
 */
public class Login {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Scanner scanL = new Scanner(System.in);
        Conexao con = new Conexao();
        Conexao con2 = new Conexao();
        InformacoesMaquina info = new InformacoesMaquina();

        con.getConnection();
        con2.getConnectionAzure();
        JdbcTemplate banco = con.getConnection();
        JdbcTemplate banco2 = con2.getConnectionAzure();
        
        System.out.println("Entre com suas credenciais:");
        System.out.println("Digite seu email:");
        String email_usuario = scan.nextLine();
        System.out.println("Digite sua senha:");
        String senha_usuario = scanL.nextLine();

        String selectRealizarLogin = "SELECT id_maquina FROM Maquina \n"
                + "WHERE email_maquina = '" + email_usuario + "' \n"
                + "and senha_maquina = '" + senha_usuario + "';";

        List loginSelect = banco.queryForList(selectRealizarLogin);
        List loginSelect2 = banco2.queryForList(selectRealizarLogin);

        String pathLogLoginError = "Logs/logs-error-login.txt";
        String pathLogLoginSucess = "Logs/logs-sucess-login.txt";

        if (!(loginSelect.isEmpty()) && !(loginSelect2.isEmpty())) {
            
            System.out.println("Login realizado com sucesso!!!\n Data e hora: ");
            EmpresaMaquina login;
            
            login = banco.queryForObject(selectRealizarLogin, new BeanPropertyRowMapper<>(EmpresaMaquina.class));
            info.inserirInformacoesBanco(login.getIdMaquina());
            
            login = banco2.queryForObject(selectRealizarLogin, new BeanPropertyRowMapper<>(EmpresaMaquina.class));
            info.inserirInformacoesBancoAzure(login.getIdMaquina());

        } else {
            System.out.println("Erro ao realizar Login!!!");
        }
    }
}
