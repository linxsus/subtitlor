package com.subtitlor.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.subtitlor.dao.TraduitSrtDao;
import com.subtitlor.utilities.TraduitSrtPage;

/**
 * @author xavier
 *
 */
public class TraduitSrtDaoFile implements TraduitSrtDao 
{
	
	private ArrayList<TraduitSrtPage> traduitSrtPages=null;
	private String fileName;
	private String chemin;

	
	/**
	 * constructeur de la class
	 * 
	 * @param parameter (string[])
	 * structure de 
     *    String [1] parameter = {
     *                 fileName,
     *                 chemin};
	 *  nom du chemin complet du fichier
	 *
	 */
	
	public TraduitSrtDaoFile(String[] parameter)
	{
		setParameter(parameter);
	}
	
	
	
	/**
	 * ouverture du fichier et décodage 
	 */
	
	private void init()
	{
		ArrayList<String>  textBrute = new ArrayList<String>();
		BufferedReader br=null;
		
		
		try
		{
			//ouverture du fichier
			br = new BufferedReader(new InputStreamReader(new FileInputStream(chemin+fileName), "UTF-8"));
			String line;
			// TODO
			// enregistrement du fichier brute en mémoire pour accélérer les traitements.
			// il y a sûrement plus propre.
			while ((line = br.readLine()) != null)
			{
				textBrute.add(line);
			}
		} 
		catch (Exception e) 
		{
			System.out.println("erreur lors de l'ouverture du fichier "+ chemin+fileName);
    		System.out.println(e);
		}
		finally
		{
			try 
			{
				//fermeture du fichier
				br.close();
			}
			catch (Exception e) 
			{
				System.out.println("erreur dans la fermeture du fichier "+chemin+fileName);
				System.out.println(e);
			}
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
			// corrige un bug si le 1er caractère est le caractère pour dire que c'est de l'UTF8  
			if (!line.equals("") && Character.isIdentifierIgnorable(line.charAt(0)) )
			{
				  line = line.substring(1);
			}
					
			if (newSubtitle) // si on est sur une nouvelle page
			{
				try
				{
					//on récupère le N° de la page  
					index=Integer.parseInt(line)-1; //gestion hasardeuse il ne faut pas de trou (de page manquante) dans le fichier source
					// TODO une verification de la valeur index devrait être faite
					
					
					//on crée la nouvelle page dans le résultat avec comme index le N° de page  
					result.add(index,new TraduitSrtPage());
					result.get(index).setNumPage(index+1);
					
					newSubtitle=false; // la prochaine ligne de textbrut n'est pas une page 
					newTime=true; // la prochaine ligne de textbrut est le temps d'une page
				}
				catch (Exception e)
				{
					System.out.println("erreur dans le decodage du fichier "+fileName);
					System.out.println(e);
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
		BufferedWriter bW=null;
		try
		{
			// ouverture du fichier en écriture
			
			bW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(chemin+fileName), "UTF-8"));

			
			for (String tmp:textbrute(pages)) // on transform toutes les pages en textbrute et pour chaque ligne 
			{
				// TODO a voir l'utiliter de cette ligne
				String chaine = String.valueOf(tmp); //
				// si ce n'est pas une chaine vide on l'ecrit
				if (chaine != null)
				{
					bW.write(chaine);
					bW.newLine();
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("erreur dans l'ecriture du fichier "+chemin+fileName);
			System.out.println(e);
		}
		finally
		{
			try
			{
				//fermeture du fichier
				bW.close();
			} 
			catch (IOException e)
			{
				System.out.println("erreur dans la fermeture du fichier "+chemin+fileName);
				System.out.println(e);
			}
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
			resultat.add(String.valueOf(traditSrt.getNumPage()));
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
	public ArrayList<TraduitSrtPage> read() 
	{
		return traduitSrtPages;
	}

	@Override
	public void setParameter(String[] parameter)
	{		
		//si le nom du fichier a changer on refait une initialisation 
		switch (parameter.length)
		{
		  case 2 :chemin=parameter[1];
		  case 1 :fileName=parameter[0];
		  break;
		// TODO une gestion plus pousser de l'erreur serait un plus
		  default: System.out.println("erreur dans le nombre d'argument passer a setParameter de TraduitSrtDaoFile");
		};
		init();
	}


	@Override
	public String[] getParameter()
	{
		String[] parameter={fileName,chemin};
		return parameter;
	}
	
}
