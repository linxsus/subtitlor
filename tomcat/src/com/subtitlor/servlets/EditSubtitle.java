package com.subtitlor.servlets;
 
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subtitlor.dao.DaoFactory;
import com.subtitlor.dao.TraduitSrtDao;
import com.subtitlor.utilities.TraduitSrtTraitement;

@WebServlet("/EditSubtitle")
@MultipartConfig() public class EditSubtitle extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TraduitSrtDao traduitSrtDaoTempo;
	private TraduitSrtDao traduitSrtDaoIn;
	private TraduitSrtDao traduitSrtDaoOut;
	private TraduitSrtTraitement traitement;
	
	// initialisation des variables au d�marrage du servlet.
	
	public void init() throws ServletException {
		
		// on r�cup�r� une instance sql par le dao factory.
		ServletContext context = getServletContext();
		DaoFactory daoFactory=null;
		daoFactory = DaoFactory.getInstance(context);
		traduitSrtDaoTempo = daoFactory.getTempo();
		traduitSrtDaoIn = daoFactory.getIn();
		traduitSrtDaoOut = daoFactory.getOut();         
       		
		// et on le met dans la base de donn�e
		traduitSrtDaoTempo.write(traduitSrtDaoIn.read());
		
		// on cr�e une instance traitement
         traitement=new TraduitSrtTraitement();
         
		
	}
	
	
	// traitement d'une demande d'affichage de la page
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// on r�cup�re les sous titre dans la base temporaire.
		request.setAttribute("subtitles", traduitSrtDaoTempo.read());
		
		// code qui ne doit plus servire
		//request.setAttribute("FileNameSource", FileNameSource);
		
		// demande d'affichage de la page
		this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
	}

	
	// traitement de l'envoie d'un formulaire 
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
	//si on appuis sur le bouton charger
		String donnee=request.getParameter("charger");	
		System.out.println(donnee);
		
		if (donnee!=null && !donnee.isEmpty()) 
		{ // alors 
			//on charge le fichier 
			traitement.chargement(request,response,traduitSrtDaoTempo);
			//on d�sactive le bouton chargement 
			request.setAttribute("FileNameDestination", "");
		}
		else
	    { // sinon 
		  // on enregistre les modifs dans la base temporaire 	
		traitement.execut(request,response,traduitSrtDaoTempo);
		  // on cree aussi le fichier de sortie
		traduitSrtDaoOut.write(traduitSrtDaoTempo.read());
		  // on active le bouton chargement
		request.setAttribute("FileNameDestination", "toto");
		}
		
		// on demande un affichage de la page 	
		doGet(request,response);
	}

}
