package com.subtitlor.dao;

import javax.servlet.ServletContext;

public class DaoFactory {
    private String [] parametereSQL= {
    		"jdbc:mysql://localhost:3306/subtitlor", //url
    		"xavier",                                //login
    		"challans"};                             //password
    private String[] FileNameSource = {
    		"/password_presentation.srt", //fichier qui est charger par default
            ""};                          //chemin du fichier ne pas modifier ici car on prend par default le chemin du context
	private String[] FileNameDestination ={
			"/sortie.srt",                //nom du fichier que l'on va uploader
            ""};                          //chemin du fichier ne pas modifier ici car on prend par default le chemin du context     

       
/**
 * constructeur de la class
 * 
 * @param ServletContext context
 *  Récupère le context du servlet
 */
    
    DaoFactory(ServletContext context) {
        FileNameSource[1]=context.getRealPath(""); //on initialise le chemin par default
        FileNameDestination[1]=context.getRealPath(""); //on initialise le chemin par default
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
    	return new TraduitSrtDaoFile(FileNameSource);
    }

    
    /**
     * crée une instance pour la destination 
     * 
     * @return TraduitSrtDao
     * 
     * retourne un object de type TraduitSrtDao 
     */
    
    public TraduitSrtDao getOut() {
    	return new TraduitSrtDaoFile(FileNameDestination);
    }
    
   
}
