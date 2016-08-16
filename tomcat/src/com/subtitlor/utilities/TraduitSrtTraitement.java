package com.subtitlor.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.subtitlor.dao.TraduitSrtDao;





public class TraduitSrtTraitement {
	
    public static final int TAILLE_TAMPON = 10240;
    public static final String CHEMIN_FICHIERS = "./"; 
    
	public void execut(HttpServletRequest request, HttpServletResponse response, TraduitSrtDao traduitSrtDao) {

		
		ArrayList<TraduitSrtPage> traduitSrt=traduitSrtDao.read();
		for (TraduitSrtPage ligne:traduitSrt)
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
	
	public String chargement(HttpServletRequest request, HttpServletResponse response, TraduitSrtDao traduitSrtDao) throws IOException, ServletException {
        // On récupère le champ description comme d'habitude
		
        String description = request.getParameter("description");
        request.setAttribute("description", description );

        // On récupère le champ du fichier
        Part part = request.getPart("FileNameSource");
            
        // On vérifie qu'on a bien reçu un fichier
        String nomFichier = getNomFichier(part);
        
        System.out.println(nomFichier);
        
        // Si on a bien un fichier
        if (nomFichier != null && !nomFichier.isEmpty()) {
            String nomChamp = part.getName();
            // Corrige un bug du fonctionnement d'Internet Explorer
             nomFichier = nomFichier.substring(nomFichier.lastIndexOf('/') + 1)
                    .substring(nomFichier.lastIndexOf('\\') + 1);

            // On écrit définitivement le fichier sur le disque
            ecrireFichier(part, nomFichier, CHEMIN_FICHIERS);

            request.setAttribute(nomChamp, nomFichier);
        }

        //this.getServletContext().getRequestDispatcher("/WEB-INF/bonjour.jsp").forward(request, response);
      return nomFichier;  
    }

    private void ecrireFichier( Part part, String nomFichier, String chemin ) throws IOException {
        //BufferedInputStream entree = null;
        //BufferedOutputStream sortie = null;
        BufferedWriter sortie=null;
        BufferedReader entree=null;
		
        
        try 
        {
            //entree = new BufferedInputStream(part.getInputStream(), TAILLE_TAMPON);
            //sortie = new BufferedOutputStream(new FileOutputStream(new File(chemin + nomFichier)), TAILLE_TAMPON);
        	entree = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
            
            sortie = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(chemin + nomFichier), "UTF-8"));
            String line;
            
/*
            while ((longueur = entree.read(tampon)) > 0) 
            {
                sortie.write(tampon, 0, longueur);
            }
*/
            while ((line = entree.readLine()) != null) {
            	String chaine = String.valueOf(line); //
				// si ce n'est pas une chaine vide on l'ecrit
            	System.out.println(line);
				if (chaine != null) {
					sortie.write(chaine);
					sortie.newLine();
				}
			}
            System.out.println("ok dans l'ecriture du fichier chargement "+chemin+" / "+ nomFichier);
        } 
        catch (Exception e)
		{
			System.out.println("erreur dans l'ecriture du fichier chargement "+chemin+" / "+ nomFichier);
			System.out.println(e);
		}
        
        finally {
            try {
                sortie.close();
            } catch (IOException ignore) {
            	System.out.println("erreur dans l'ignore du fichier chargement "+chemin+" / "+ nomFichier);
            }
            catch (Exception e)
    		{
    			System.out.println("erreur dans l'ecriture du fichier chargement "+chemin+" / "+ nomFichier);
    			System.out.println(e);
    		}
            try {
                entree.close();
            } catch (IOException ignore) {
            	System.out.println("erreur dans l'ignore du fichier chargement "+chemin+" / "+ nomFichier);
            }
            catch (Exception e)
    		{
    			System.out.println("erreur dans l'ecriture du fichier chargement "+chemin+" / "+ nomFichier);
    			System.out.println(e);
    		}
        }
    }
    
    private static String getNomFichier( Part part ) {
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            if ( contentDisposition.trim().startsWith( "filename" ) ) {
                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        }
        return null;
    }   
}	

	


