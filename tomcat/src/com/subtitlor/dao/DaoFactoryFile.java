package com.subtitlor.dao;

import javax.servlet.ServletContext;

public class DaoFactoryFile {

	private String FILE_NAME = "/WEB-INF/password_presentation.srt";
	
	public DaoFactoryFile(){
	ServletContext context = getServletContext();
	subtitles = new TraduitSrtDaoFile(context.getRealPath(FILE_NAME));
	}
	
}
