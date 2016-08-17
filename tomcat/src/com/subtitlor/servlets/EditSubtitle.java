package com.subtitlor.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subtitlor.dao.DaoFactory;
import com.subtitlor.dao.TraduitSrtDao;
import com.subtitlor.utilities.TraduitSrtImportFichier;
import com.subtitlor.utilities.TraduitSrtPage;
import com.subtitlor.utilities.TraduitSrtPagination;
import com.subtitlor.utilities.TraduitSrtTraitement;

@WebServlet("/EditSubtitle")
@MultipartConfig()
public class EditSubtitle extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private TraduitSrtDao traduitSrtDaoTempo;
	private TraduitSrtDao traduitSrtDaoIn;
	private TraduitSrtDao traduitSrtDaoOut;
	private boolean fichier;

	// initialisation des variables au démarrage du servlet.

	public void init() throws ServletException {

		// on récupéré les instance par le dao factory.
		ServletContext context = getServletContext();
		DaoFactory daoFactory = null;
		daoFactory = DaoFactory.getInstance(context);
		traduitSrtDaoTempo = daoFactory.getTempo();
		traduitSrtDaoIn = daoFactory.getIn();
		traduitSrtDaoOut = daoFactory.getOut();

		// et on le met le fichier d'entree dans la base de donnée
		traduitSrtDaoTempo.write(traduitSrtDaoIn.read());
		
		fichier=false;

	}

	// traitement d'une demande d'affichage de la page

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (fichier)
			request.setAttribute("FileNameDestination", traduitSrtDaoOut.getParameter()[0]);// !!!attention car on ne prend que le nom du fichier on estime que le chemin est bien celui fournis par le context 
		else
			request.setAttribute("FileNameDestination", "");
		ArrayList<TraduitSrtPage> partiel=TraduitSrtPagination.pagination(request,response, traduitSrtDaoTempo.read());
		
		// on récupère les sous titre dans la base temporaire.
		request.setAttribute("subtitles", partiel);

		// demande d'affichage de la page
		this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
	}

	// traitement de l'envoie d'un formulaire

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// si on appuis sur le bouton charger
		String donnee = request.getParameter("charger");
		// on vérifie aussi il qu'il y a fichier cela permet de contourner un bug lorque l'on appuis sur entree dans le formulaire
		// cela n'est pas très propre
		String nomFichier = TraduitSrtImportFichier.getNomFichier(request.getPart("FileNameSource")); 
		if (donnee != null && !donnee.isEmpty() && nomFichier != null && !nomFichier.isEmpty()) 
		{ // alors
			  // on récupère le fichier dans traduitSrtDaoIn
			  TraduitSrtImportFichier.chargement(request, response, traduitSrtDaoIn);
			  // et on le met dans la base de donnée
			  traduitSrtDaoTempo.write(traduitSrtDaoIn.read());
			  // on désactive le bouton chargement
			  fichier=false;
		} else { // sinon
					// on enregistre les modifs dans la base temporaire
			TraduitSrtTraitement.execut(request, response, traduitSrtDaoTempo);
			// on crée aussi le fichier de sortie
			traduitSrtDaoOut.write(traduitSrtDaoTempo.read());
			// on active le bouton chargement
			fichier=true;
		}

		// on demande un affichage de la page
		doGet(request, response);
	}

}
