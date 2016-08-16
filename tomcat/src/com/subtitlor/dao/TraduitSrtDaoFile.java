package com.subtitlor.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.subtitlor.dao.TraduitSrtDao;
import com.subtitlor.utilities.TraduitSrtPage;

/**
 * @author xavier
 *
 */
public class TraduitSrtDaoFile implements TraduitSrtDao {
	
	private ArrayList<TraduitSrtPage> traduitSrtPages=null;
	private String fileName;

	
	/**
	 * constructeur de la class
	 * 
	 * @param parameter (string[])
	 * structure de 
 *    String [1] parameter = {
 *                 fileName};
	 *  nom du chemin complet du fichier
	 *
	 */
	
	public TraduitSrtDaoFile(String[] parameter) {
		setParameter(parameter);
	}
	
	
	
	/**
	 * constructeur de la class
	 * 
	 * @param fileName (string)
	 *  nom du chemin complet du fichier
	 *
	 */
	public TraduitSrtDaoFile(String fileName) {
		String [] parameter={fileName};
		setParameter(parameter);
	}
	
	
	
	/**
	 * ouverture du fichier et décodage 
	 */
	
	private void init(){
		ArrayList<String>  textBrute = new ArrayList<String>();
		BufferedReader br;
		
		
		try {
			//ouverture du fichier
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			String line;
			// TODO
			// enregistrement du fichier brute en mémoire pour accélérer les traitements.
			// il y a surement plus propre.
			while ((line = br.readLine()) != null) {
				textBrute.add(line);
			}
			//fermeture du fichier
			br.close();
		} catch (IOException e) {
			System.out.println("erreur lors de l'ouverture du fichier "+ fileName);
    		System.out.println(e);
		}
		// décodage du fichier pour le mettre dans la structure traduitSrts 
		traduitSrtPages=decodage(textBrute);	
	}
	
	
	/**
	 * decodage du text brute en traduitSrt
	 * @param textBrute
	 * @return
	 */
	private ArrayList<TraduitSrtPage> decodage(ArrayList<String>  textBrute)
	{
		ArrayList<TraduitSrtPage> result=new ArrayList<TraduitSrtPage>() ;
	
		int index=0; // 
		boolean newSubtitle=true; //la prochaine ligne de textbrut est une page
		boolean newTime=false; // la prochaine ligne de textbrut n'est pas le temps d'une page 
		for (String line:textBrute) //pour chaque ligne du fichier
		{
			System.out.println(line);
			if (newSubtitle) // si on est sur une nouvelle page
			{
				try
				{
					//on récupère le N° de la page  
					index=Integer.parseInt(line)-1; //gestion hasardeuse il ne faut pas de trou (de page manquante) dans le fichier source
					// TODO une verification de la valeur index devrait être faite
					
					
					//on crée la nouvelle page dans le résultat avec comme index le N° de page  
					result.add(index,new TraduitSrtPage());
					result.get(index).setNumLigne(index+1);
					
					newSubtitle=false; // la prochaine ligne de textbrut n'est pas une page 
					newTime=true; // la prochaine ligne de textbrut est le temps d'une page
				}
				catch (Exception e)
				{
					System.out.println("erreur dans le decodage du fichier "+fileName);
					System.out.println(e);
					index=0;
					result.add(index,new TraduitSrtPage());
					result.get(index).setNumLigne(index+1);
					
					newSubtitle=false; // la prochaine ligne de textbrut n'est pas une page 
					newTime=true; // la prochaine ligne de textbrut est le temps d'une page
					System.out.println(line);
				}
			}
			else
				
			// ce bout de code est pas très correcte car il faut absolument une ligne vide (sans espace...) entre chaque affichage 
			    if (line.equals("")) //si on a une ligne vide sans espace
			    {
			    	newSubtitle=true; // la prochaine ligne de textbrut est une nouvelle page
			    }
			    else
			    	if (newTime) //si la ligne de textbrut est le temps d'une page 
			    	{
			    		// on enregistre le temps de la page
			    		result.get(index).setTime(line); 
			    		newTime=false; // la prochaine ligne de textbrut n'est pas le temps d'une page
			    	}
			    	else // on est ni sur une nouvelle page ni sur le temps d'une page donc on est sur une ligne a traduire
			    {
			    	
			    		//ajout de la ligne a traduire
			    		result.get(index).getOriginal().add(line);
			    }
			    
		}
		return result;
	}
	

	@Override
	public void write(ArrayList<TraduitSrtPage> pages) 
	{
		try
		{
			// ouverture du fichier en écriture
			BufferedWriter bW;
			bW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));

			
			for (String tmp:textbrute(pages)) // on transform toutes les pages en textbrute et pour chaque ligne 
			{
				// TODO a voir l'utiliter de cette ligne
				String chaine = String.valueOf(tmp); //
				// si ce n'est pas une chaine vide on l'ecrit
				if (chaine != null) {
					bW.write(chaine);
					bW.newLine();
				}
			}
			//fermeture du fichier
			bW.close();
		}
		catch (Exception e)
		{
			System.out.println("erreur dans l'ecriture du fichier "+fileName);
			System.out.println(e);
		}
		
	}
	
	/**
	 * transform les pages en text brute
	 * 
	 * @param traduitSrtPage
	 * @return
	 */
	private ArrayList<String> textbrute(ArrayList<TraduitSrtPage>  Pages)
	{
		ArrayList<String> resultat= new ArrayList<String>();
		
		for (TraduitSrtPage traditSrt:Pages) //pour chaque page
		{
			// on récupère le N° de page
			resultat.add(String.valueOf(traditSrt.getNumLigne()));
			// on récupère le temps de la page
			resultat.add(traditSrt.getTime());
			//on récupère les différente ligne
			for(String ligne:traditSrt.getTraduit())
			{
				resultat.add(ligne);
			}
			// on signal la fin de page
			resultat.add("");
		}
		return resultat;
	}

	@Override
	public ArrayList<TraduitSrtPage> read() {

		return traduitSrtPages;
	}

	@Override
	public void setParameter(String[] parameter) {
		// TODO il faut faire une verification des parameter
		
		//si le nom du fichier a changer on refait une initialisation 
		if (fileName!=parameter[0]) 
		{
			fileName=parameter[0];
			init();
		}
	}


	@Override
	public String[] getParameter() {
		String[] parameter={fileName};
		return parameter;
	}
	
}
