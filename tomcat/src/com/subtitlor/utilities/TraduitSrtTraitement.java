package com.subtitlor.utilities;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subtitlor.dao.TraduitSrtDao;

public class TraduitSrtTraitement {

	public void execut(HttpServletRequest request, HttpServletResponse response, TraduitSrtDao traduitSrtDao) {
		// TODO Auto-generated method stub
		
		ArrayList<TraduitSrt> traduitSrt=traduitSrtDao.read();
		for (TraduitSrt ligne:traduitSrt)
		{
			ArrayList<String> tempo=new ArrayList<String>();
			// hoo que c'est laid ce codage !!!!
			int i=0;
			String donnee;
			try {
				request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (String text:ligne.getOriginal())
			{
				
				String variable="paragraphe"+ligne.getNumLigne()+"linge"+i;
				donnee=request.getParameter(variable);
				
				if (donnee!=null && !donnee.isEmpty())
				{
					
					/*// a mon avis il y a plus propre mais bon ça corrige le bug 
					try {
						donnee=new String(donnee.getBytes(),"UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
				tempo.add(donnee);
				}
				i++;
			}
			
			
			traduitSrt.get(ligne.getNumLigne()-1).setTraduit(tempo);
			
		}
		traduitSrtDao.write(traduitSrt);
	}

}
