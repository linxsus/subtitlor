package com.subtitlor.servlets;
 
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subtitlor.dao.DaoFactorySql;
import com.subtitlor.dao.TraduitSrtDao;
import com.subtitlor.dao.TraduitSrtDaoFile;
import com.subtitlor.utilities.TraduitSrtTraitement;

@WebServlet("/EditSubtitle")
@MultipartConfig() public class EditSubtitle extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String FileNameSource = "/password_presentation.srt"; //fichier qui est charger par default
	private String FileNameDestination = "/sortie.srt";  //nom du fichier que l'on va uploader
	
	private TraduitSrtDao traduitSrtDaoSql;
	private TraduitSrtDao traduitSrtDaoFileIn;
	private TraduitSrtDao traduitSrtDaoFileOut;
	private TraduitSrtTraitement traitement;
	
	// initialisation des variables au démarrage du servlet.
	
	public void init() throws ServletException {
		
		// on récupéré une instance sql par le dao factory.
		{
         DaoFactorySql daoFactory = DaoFactorySql.getInstance();
         this.traduitSrtDaoSql = daoFactory.getTraduitSrtDao();
	    }
		
	    //on charge le context car les fichier (par default et sortie) sont dans webContent.
         ServletContext context = getServletContext();
       //on cree une instance pour le fichier source (fichier par default).
         traduitSrtDaoFileIn = new TraduitSrtDaoFile(context.getRealPath(FileNameSource));
		
		// et on le met dans la base de donnée
		traduitSrtDaoSql.write(traduitSrtDaoFileIn.read());
		
		// on crée une instance traitement
         traitement=new TraduitSrtTraitement();
         
        // on crée une instance pour le fichier de sortie.
         traduitSrtDaoFileOut = new TraduitSrtDaoFile(context.getRealPath(FileNameDestination));
		
	}
	
	
	// traitement d'une demande d'affichage de la page
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// on récupère les sous titre dans la base temporaire.
		request.setAttribute("subtitles", traduitSrtDaoSql.read());
		
		// code qui ne doit plus servire
		//request.setAttribute("FileNameSource", FileNameSource);
		
		// demande d'affichage de la page
		this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
	}

	
	// traitement de l'envoie d'un formulaire 
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
	//si on appuis sur le bouton charger
		String donnee=request.getParameter("charger");	
		if (donnee!=null && !donnee.isEmpty()) 
		{ // alors 
			//on charge le fichier 
			traitement.chargement(request,response,traduitSrtDaoSql);
			//on désactive le bouton chargement 
			request.setAttribute("FileNameDestination", "");
		}
		else
	    { // sinon 
		  // on enregistre les modifs dans la base temporaire 	
		traitement.execut(request,response,traduitSrtDaoSql);
		  // on cree aussi le fichier de sortie
		traduitSrtDaoFileOut.write(traduitSrtDaoSql.read());
		  // on active le bouton chargement
		request.setAttribute("FileNameDestination", FileNameDestination);
		}
		
		// on demande un affichage de la page 	
		doGet(request,response);
	}

}
