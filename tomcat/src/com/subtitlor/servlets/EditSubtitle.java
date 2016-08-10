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
	private String FileNameSource = "/WEB-INF/password_presentation.srt";
	private TraduitSrtDao traduitSrtDaoSql;
	private TraduitSrtDao traduitSrtDaoFileIn;
	private TraduitSrtDao traduitSrtDaoFileOut;
	private TraduitSrtTraitement traitement;
	private String FileNameDestination = "/sortie.srt";
	
	public void init() throws ServletException {
        DaoFactorySql daoFactory = DaoFactorySql.getInstance();
        this.traduitSrtDaoSql = daoFactory.getTraduitSrtDao();
        ServletContext context = getServletContext();
        traduitSrtDaoFileIn = new TraduitSrtDaoFile(context.getRealPath(FileNameSource));
        traduitSrtDaoSql.write(traduitSrtDaoFileIn.read());
        traitement=new TraduitSrtTraitement();
        traduitSrtDaoFileOut = new TraduitSrtDaoFile(context.getRealPath(FileNameDestination));
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("subtitles", traduitSrtDaoSql.read());
		request.setAttribute("FileNameSource", FileNameSource);
		
		this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String donnee;
		donnee=request.getParameter("charger");
		
	//si on appuis sur le bouton charger
		if (donnee!=null && !donnee.isEmpty()) 
			
		{
			traitement.chargement(request,response,traduitSrtDaoSql);
			request.setAttribute("FileNameDestination", null);
		}
		else
		{
			System.out.println("test2");
		traitement.execut(request,response,traduitSrtDaoSql);
		traduitSrtDaoFileOut.write(traduitSrtDaoSql.read());
		request.setAttribute("FileNameDestination", FileNameDestination);
		}
		
			
		doGet(request,response);
	}

}
