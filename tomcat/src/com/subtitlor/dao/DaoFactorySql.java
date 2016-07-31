package com.subtitlor.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactorySql {
    private String url;
    private String username;
    private String password;

    DaoFactorySql(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DaoFactorySql getInstance() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
        	System.out.println(e);        	
        }
        DaoFactorySql instance = new DaoFactorySql(
                "jdbc:mysql://localhost:3306/subtitlor", "xavier", "challans");
        return instance;
    }

    public Connection getConnection() throws SQLException {
    	return DriverManager.getConnection(url, username, password);
    }

    // Récupération du Dao
    public TraduitSrtDao getTraduitSrtDao() {
        return new TraduitSrtDaoMysql (this);
    }
}
