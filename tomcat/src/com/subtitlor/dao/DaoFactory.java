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
       
/**
 * constructeur de la class
 * 
 * @param ServletContext context
 *  Récupère le context du servlet
 */
    
    DaoFactory(ServletContext context) {
        this.context=context;
    }

    /**
     * permet de crée une instance 
     * @param context
     * @return
     *
     * Récupère le context du servlet
     *
     * retourne une instance de la class
     */
    
    public static DaoFactory getInstance(ServletContext context) {
       
        DaoFactory instance = new DaoFactory(context);
        return instance;
    }
    
    /**
     * crée une instance de la base temporaire 
     * 
     * @return TraduitSrtDao
     * 
     * retourne un object de type TraduitSrtDao 
     */
    
    public TraduitSrtDao getTempo() {
    	return new TraduitSrtDaoMysql (parametereSQL);
    }

    
    /**
     * crée une instance pour la source
     * 
     * @return TraduitSrtDao
     * 
     * retourne un object de type TraduitSrtDao 
     */
    
    public TraduitSrtDao getIn() {
    	return new TraduitSrtDaoFile(context.getRealPath(FileNameSource));
    }

    
    /**
     * crée une instance pour la destination 
     * 
     * @return TraduitSrtDao
     * 
     * retourne un object de type TraduitSrtDao 
     */
    
    public TraduitSrtDao getOut() {
    	return new TraduitSrtDaoFile(context.getRealPath(FileNameDestination));
    }
    
   
}
