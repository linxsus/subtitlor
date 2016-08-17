package com.subtitlor.utilities;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * gestion des variables pour la pagination
 * 
 * !! attention on modifie pages et request
 * 
 * @author xavier
 *
 */
public class TraduitSrtPagination {
	private static int NB_PAGINATION=10; // nb de pagination afficher 
	private static int NB_PAGE=20; //nb de page de la video
	
	
	public static ArrayList<TraduitSrtPage> pagination(HttpServletRequest request, HttpServletResponse response,ArrayList<TraduitSrtPage> pages)
	{
		
		int totalPagi;
		if (pages.size()%NB_PAGE==0)
			totalPagi=pages.size()/NB_PAGE;
		else
			totalPagi=(pages.size()/NB_PAGE)+1;
		// si pagination n'est pas initialiser on met la valeur 1 
		String pagination = request.getParameter("pagination");
		int pagi;
				
		if (pagination == null || pagination.isEmpty())
			{
			 pagi=1; // page 1 par default
			}
		else
		   pagi=Integer.parseInt(pagination);
		
		if ((pagi-1)>(NB_PAGINATION/2) ) // si pagi est plus grand que la moitié du nb de pagination afficher
		{    
			request.setAttribute("chevronGauche", "ok"); // affichage des chevronGauche
			request.setAttribute("debutPagi", (pagi-(NB_PAGINATION/2)));      // le debut est la page 1
		}
		else
		{
			request.setAttribute("chevronGauche", null);// on cache les chevronGauche
			request.setAttribute("debutPagi", 1 );      // le debut est la page active moins la moitié du nb de pagination afficher
		}  
		// affichage des chevronDroit
		if (pagi<(totalPagi-(NB_PAGINATION/2)) ) // si pagi est plus petit que la totalite de pagination moin la moitié du nb de pagination afficher 
		{	
			request.setAttribute("chevronDroit", "ok");// affichage des chevronDroit
		    request.setAttribute("finPagi", (pagi+(NB_PAGINATION/2))); // la fin est la page active plus la moitié du nb de pagination afficher
		}
		else
		{
			request.setAttribute("chevronDroit", null);// on cache les chevronDroit
			request.setAttribute("finPagi", totalPagi);// la fin est le nb max de pagination
		}
				
		ArrayList<TraduitSrtPage> resultat=new ArrayList<TraduitSrtPage>() ;
		int nbPage=NB_PAGE;
		// gestion de la fin de fichier 
		if ((pagi*NB_PAGE)>pages.size())
			nbPage=pages.size()-((pagi-1)*NB_PAGE);
		
		// on copie la partie voulue dans resultat
		for(int i=0;i<nbPage;i++)
		{
			resultat.add(i,pages.get((pagi-1)*NB_PAGE+i));
		}
		request.setAttribute("pagination", pagi+"");
		return resultat;
	}
}
