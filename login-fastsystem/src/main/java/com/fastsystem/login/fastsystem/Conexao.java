package com.fastsystem.login.fastsystem;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {
    private JdbcTemplate connection;
    private JdbcTemplate connectionAzure;

    public Conexao() {
        BasicDataSource dataSource = new BasicDataSource();
        BasicDataSource dataSourceAzure = new BasicDataSource();
        
        //MySQL no Docker
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://172.17.0.2:3306/FastSystem?useTimezone=true&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("urubu100");
        
        //SQL Server na Azure
         dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://fast-system-server.database.windows.net:1433;database=FastSystem;encryp t=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;");
        dataSource.setUsername("admin-fast-system");  //221-1adsc-grupo10@bandtec.com.br
        dataSource.setPassword("#Gfgrupo10");
        
        //Constructor
        this.connection = new JdbcTemplate(dataSource);
        this.connection = new JdbcTemplate(dataSourceAzure);
    }
    
    public JdbcTemplate getConnection() {
        return connection;
    }
    
    public JdbcTemplate getConnectionAzure() {
        return connectionAzure;
    }
}
