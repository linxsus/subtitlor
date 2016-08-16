package com.subtitlor.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


import com.subtitlor.dao.TraduitSrtDao;

public class TraduitSrtImportFichier
{
	public static final int TAILLE_TAMPON = 10240;
    
    public void chargement(HttpServletRequest request, HttpServletResponse response, TraduitSrtDao traduitSrtIn) 
    {
        // On récupère le champ du fichier
        Part part=null;
		try {
			part = request.getPart("FileNameSource");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            
        // On vérifie qu'on a bien reçu un fichier
        String nomFichier = getNomFichier(part);
        
        // Si on a bien un fichier
        if (nomFichier != null && !nomFichier.isEmpty()) {
            //String nomChamp = part.getName();
            // Corrige un bug du fonctionnement d'Internet Explorer
             nomFichier = nomFichier.substring(nomFichier.lastIndexOf('/') + 1)
                    .substring(nomFichier.lastIndexOf('\\') + 1);

             // on récupère le chemin des fichier source.
             String chemin=traduitSrtIn.getParameter()[1]; 
             
            // On écrit définitivement le fichier sur le disque
            ecrireFichier(part, nomFichier, chemin);
            
            // on charge le fichier dans traduitSrtIn
            String[] parameter={nomFichier,chemin};
            traduitSrtIn.setParameter(parameter);      
        }    
        
    }

    private void ecrireFichier( Part part, String nomFichier, String chemin ) {
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
 
        try 
        {
            entree = new BufferedInputStream(part.getInputStream(), TAILLE_TAMPON);
            sortie = new BufferedOutputStream(new FileOutputStream(new File(chemin + nomFichier)), TAILLE_TAMPON);

            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur;

            while ((longueur = entree.read(tampon)) > 0) 
            {
                sortie.write(tampon, 0, longueur);
            }
        } 
        catch (Exception e)
		{
			System.out.println("erreur dans l'ecriture du fichier chargement "+chemin+" / "+ nomFichier);
			System.out.println(e);
		}
        
        finally 
        {
            try 
            {
                sortie.close();
            } catch (IOException ignore) {
            	System.out.println("erreur dans l'ignore du fichier sotie "+chemin+" / "+ nomFichier);
            	System.out.println(ignore);
            }
            catch (Exception e)
    		{
    			System.out.println("erreur dans l'ecriture du fichier chargement "+chemin+" / "+ nomFichier);
    			System.out.println(e);
    		}
            
            try 
            {
                entree.close();
            } 
            catch (IOException ignore) 
            {
            	System.out.println("erreur dans l'ignore du fichier entree "+chemin+" / "+ nomFichier);
            	System.out.println(ignore);
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
