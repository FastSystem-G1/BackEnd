package com.fastsystem.login.fastsystem;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {
    private JdbcTemplate connection;
    private JdbcTemplate connectionAzure;

    public Conexao() {
        BasicDataSource dataSource = new BasicDataSource();
        BasicDataSource dataSourceAzure = new BasicDataSource();
        
        //MySQL local!!!
        //MySQL no Docker
        // dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // dataSource.setUrl("jdbc:mysql://localhost:3306/FastSystem?useTimezone=true&serverTimezone=UTC");
        // dataSource.setUsername("root");
        // dataSource.setPassword("");
        
        //MySQL no Docker
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://172.28.5.0:3306/FastSystem?useTimezone=true&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("urubu100");
        
        //SQL Server na Azure
        try{
            dataSourceAzure.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            dataSourceAzure.setUrl("jdbc:sqlserver://fast-system-server.database.windows.net:1433;database=FastSystem;encryp t=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;");
            dataSourceAzure.setUsername("admin-fast-system");  //221-1adsc-grupo10@bandtec.com.br
            dataSourceAzure.setPassword("#Gfgrupo10");
        }
        catch(Exception e){
            System.out.println("Nao entrou");
        }
        
        
        //Constructor
        this.connection = new JdbcTemplate(dataSource);
        this.connectionAzure = new JdbcTemplate(dataSourceAzure);
    }
    
    public JdbcTemplate getConnection() {
        return connection;
    }
    
    public JdbcTemplate getConnectionAzure() {
        return connectionAzure;
    }
}
