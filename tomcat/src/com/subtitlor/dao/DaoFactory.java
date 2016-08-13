package com.subtitlor.dao;

import javax.servlet.ServletContext;

public class DaoFactory {
    private String [] parametereSQL= {
    		"jdbc:mysql://localhost:3306/subtitlor", //url
    		"xavier",                                //login
    		"challans"};                             //password
    private String FileNameSource = "/password_presentation.srt"; //fichier qui est charger par default
	private String FileNameDestination = "/sortie.srt";  //nom du fichier que l'on va uploader
     
    private ServletContext context;
       

    DaoFactory(ServletContext context) {
        this.context=context;
    }

    public static DaoFactory getInstance(ServletContext context) {
       
        DaoFactory instance = new DaoFactory(context);
        return instance;
    }
/*
    // a supprimer a la fin
    public Connection getConnection() throws SQLException {
    	return DriverManager.getConnection(url, username, password);
    }

    // Récupération du Dao
    // a supprimer a la fin
    public TraduitSrtDao getTraduitSrtDao() {
        return new TraduitSrtDaoMysql (this);
    }
*/    
    
    public TraduitSrtDao getTempo() {
    	return new TraduitSrtDaoMysql (parametereSQL);
    }

    public TraduitSrtDao getIn() {
    	return new TraduitSrtDaoFile(context.getRealPath(FileNameSource));
    }

    public TraduitSrtDao getOut() {
    	return new TraduitSrtDaoFile(context.getRealPath(FileNameDestination));
    }
    
   
}
