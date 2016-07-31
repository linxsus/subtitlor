package com.subtitlor.dao;

import javax.servlet.ServletContext;

public class DaoFactoryFile {

	private String FILE_NAME = "/WEB-INF/password_presentation.srt";
	
	public DaoFactoryFile(){
	subtitles = new TraduitSrtDaoFile(context.getRealPath(FILE_NAME));
	}
	
}
