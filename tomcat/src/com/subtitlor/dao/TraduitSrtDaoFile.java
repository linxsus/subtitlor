package com.subtitlor.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.subtitlor.dao.TraduitSrtDao;
import com.subtitlor.utilities.TraduitSrt;

import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class TraduitSrtDaoFile implements TraduitSrtDao {
	
	private ArrayList<TraduitSrt> traduitSrts=null;
	private String fileName;

	public TraduitSrtDaoFile(String fileName) {
		ArrayList<String>  textBrute = new ArrayList<String>();
		BufferedReader br;
		this.fileName=fileName;
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				textBrute.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		traduitSrts=decodage(textBrute);
	}
	
	
	private ArrayList<TraduitSrt> decodage(ArrayList<String>  textBrute)
	{
		ArrayList<TraduitSrt> result=new ArrayList<TraduitSrt>() ;
		boolean newSubtitle=true;
		int index=0;
		boolean newTime=false;
		for (String line:textBrute)
		{
			if (newSubtitle)
			{
				try
				{
					index=Integer.parseInt(line)-1; //gestion hasardeuse il ne faut pas de trou dans le fichier source
					
					result.add(index,new TraduitSrt());
					result.get(index).setNumLigne(index+1);
					
					newSubtitle=false;
					newTime=true;
				}
				catch (Exception e)
				{
					System.out.println("erreur fatal"+ e);	
				}
			}
			else
				
			// ce bout de code est pas tres correcte car il faut absolument une ligne vide (sans espace...) entre chaque affichage 
			    if (line.equals(""))
			    {
			    	newSubtitle=true;
			    }
			    else
			    	if (newTime)
			    	{
			    		result.get(index).setTime(line);
			    		newTime=false;
			    	}
			    	else
			    {
			    	result.get(index).getOriginal().add(line);
			    }
			    
		}
		return result;
	}
	

	@Override
	public void write(ArrayList<TraduitSrt> lignes) 
	{
		try
		{
			BufferedWriter bW;
			bW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));


			for (String tmp:textbrute(lignes))
			{
				String chaine = String.valueOf(tmp);
				if (chaine != null) {
					bW.write(chaine);
					bW.newLine();
				}
			}
			bW.close();
		}
		catch (Exception e)
		{
			
		}
		
	}
	private ArrayList<String> textbrute(ArrayList<TraduitSrt>  traduitSrts)
	{
		ArrayList<String> resultat= new ArrayList<String>();
		for (TraduitSrt traditSrt:traduitSrts)
		{
			resultat.add(String.valueOf(traditSrt.getNumLigne()));
			resultat.add(traditSrt.getTime());
			for(String ligne:traditSrt.getTraduit())
			{
				resultat.add(ligne);
			}
			resultat.add("");
		}
		return resultat;
	}

	@Override
	public ArrayList<TraduitSrt> read() {

		return traduitSrts;
	}
	
}
