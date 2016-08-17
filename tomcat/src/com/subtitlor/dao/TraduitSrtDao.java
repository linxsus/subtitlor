package com.subtitlor.dao;

import java.util.ArrayList;

import com.subtitlor.utilities.TraduitSrtPage;



public interface TraduitSrtDao {
	
	/**
	 * Écriture dans la base du tableau de structure TraduitSrt 
	 * @param lignes
	 *   tableau de structure TraduitSrt
	 */
	
	public void write(ArrayList<TraduitSrtPage> lignes);
	
	/**
	 * récupération dans la base du tableau de structure TraduitSrt 
	 * @param lignes
	 *   tableau de structure TraduitSrt
	 * @return
	 */
	public ArrayList<TraduitSrtPage> read();
	
	/**
	 *permet de changer le paramètre de l'object
	 * 
	 * @param String [] parameter 
	 */
	public void setParameter(String [] parameter);
	
	
	/**
	 * permet de lire les paramètres de l'object
	 * 
	 * @return string[]
	 */
	public String[] getParameter();


}
