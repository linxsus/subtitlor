package com.subtitlor.utilities;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.subtitlor.dao.TraduitSrtDao;





public class TraduitSrtTraitement {
	
    
    
	/**
	 * fonction qui permet de r�cup�rer les lignes traduite et de les mettre dans traduitSrtDao pass� en param�tre
	 * 
	 * !! attention cette fonction va modifier traduitSrtDao!!!!
	 * 
	 * @param request
	 * @param response
	 * @param traduitSrtDao
	 */
	public static void execut(HttpServletRequest request, HttpServletResponse response, TraduitSrtDao traduitSrtDao) {

		// R�cup�ration des pages original.
		ArrayList<TraduitSrtPage> partiel=TraduitSrtPagination.pagination(request,response, traduitSrtDao.read());
		
		ArrayList<TraduitSrtPage> traduitSrt=traduitSrtDao.read();
		// pour chaque page partiel.
		for (TraduitSrtPage page:partiel)
		{
			
			// on cr�e un ensemble de lignes
			ArrayList<String> lignes=new ArrayList<String>();
			
			//pour chaque ligne d'une page partiel 
			for (int i=0; i<=page.getOriginal().size();i++)
			{
				String donnee;
				
				// on lit la valeur du text traduit
				String variable="paragraphe"+page.getNumPage()+"linge"+i;
				donnee=request.getParameter(variable);
				
				// si il a �t� traduit
				if (donnee!=null && !donnee.isEmpty())
				{
					
					// a mon avis il y a plus propre mais bon �a permet de g�rer UTF-8
					try {
						donnee=new String(donnee.getBytes(),"UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				// on l'ajoute dans l'ensemble de ligne de la page
				lignes.add(donnee);
				}
			}
			
			// on ajoute les ligne traduites a la page original
			traduitSrt.get(page.getNumPage()-1).setTraduit(lignes);
			
		}
		//on �crit le r�sultat dans traduitSrtDao qui nous a �t� pass�e en param�tre.
		traduitSrtDao.write(traduitSrt);
	}
}	

	


