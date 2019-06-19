package br.com.samuelprojetos.dao;

import java.sql.*;

public class ModuloConexao {
    //metodo responsanvel por estabelecer a conexao com banco de dados

    public static Connection conector() {
        java.sql.Connection conexao = null;
        // a linha abaixo chama o drive o drive importado da biblioteca
        String driver = "com.mysql.jdbc.Driver";
        // variaveis para armazenar informaçao do banco de dados
        String url = "jdbc:mysql://localhost:3306/dbinfox";
        String user = "root";
        String password = "";
        //estabelecer a conexao com o banco de dados
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url,user, password);
            return conexao;
        } catch (Exception e) {
            //a linha abaixo serve de apoio para esclarecer o erro de conexao
            //System.out.println(e);
            
            return null;
        }
    }
}
