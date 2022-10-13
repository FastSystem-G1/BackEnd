package com.fastsystem.login.fastsystem;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {
    private JdbcTemplate connection;

    public Conexao() {
        BasicDataSource dataSource = new BasicDataSource();
        
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        //Se estiver na faculdade mudar o localhost para "localhost:3306".
        dataSource.setUrl("jdbc:mysql://localhost:3307/fastsystem?useTimezone=true&serverTimezone=UTC");
        //Se estiver na faculdade mudar o Username para "aluno" ou manter "root".
        dataSource.setUsername("root");
        //Se estiver na faculdade mudar a senha para "sptech" ou "spt3ch".
        dataSource.setPassword("Jady220922");

        this.connection = new JdbcTemplate(dataSource);
    }
    
    public JdbcTemplate getConnection() {
        return connection;
    }
}