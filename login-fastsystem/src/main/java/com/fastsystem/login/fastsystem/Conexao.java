package com.fastsystem.login.fastsystem;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {
    private JdbcTemplate connection;

    public Conexao() {
        BasicDataSource dataSource = new BasicDataSource();
        
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        //Se estiver na faculdade mudar o localhost para "localhost:3306".
        // dataSource.setUrl("jdbc:mysql://localhost:3306/FastSystem?useTimezone=true&serverTimezone=UTC");
        dataSource.setUrl("jdbc:sqlserver://fast-system-server.database.windows.net:1433;database=FastSystem;encryp t=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;");
        //Se estiver na faculdade mudar o Username para "aluno" ou manter "root".
        // dataSource.setUsername("aluno");
        dataSource.setUsername("admin-fast-system");  //221-1adsc-grupo10@bandtec.com.br
        //Se estiver na faculdade mudar a senha para "sptech" ou "spt3ch".
        // dataSource.setPassword("sptech");
        dataSource.setPassword("#Gfgrupo10");

        this.connection = new JdbcTemplate(dataSource);
    }
    
    public JdbcTemplate getConnection() {
        return connection;
    }
}
