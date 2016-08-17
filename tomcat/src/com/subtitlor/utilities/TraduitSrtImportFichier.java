package com.subtitlor.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


import com.subtitlor.dao.TraduitSrtDao;

public class TraduitSrtImportFichier
{
	public static final int TAILLE_TAMPON = 10240;
    
    /**
     * Récupère le fichier dans la requête 
     * on l'ecrit sur le disque dur
     * et on l'ecrit dans traduitSrtIn
     * 
     * !! attention lors de l'execution on modifie traduitSrtIn
     *  
     * @param request
     * @param response
     * @param traduitSrtIn
     */
	
    public static void chargement(HttpServletRequest request, HttpServletResponse response, TraduitSrtDao traduitSrtIn) 
    {
        // On récupère le champ du fichier
        Part part=null;
		try {
			part = request.getPart("FileNameSource");
		} 
		catch (Exception e) 
		{
			System.out.println("erreur dans la lecture part ");
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
            
            // on se remet sur la pagination 1
            //request.setAttribute("pagination","1"); // cela ne fonctionne pas il faudrait trouver une autre façon
        }    
        
    }

    private static void ecrireFichier( Part part, String nomFichier, String chemin ) {
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
 
        try 
        {
            // copy du fichier temporaire vers le fichier définitif
        	entree = new BufferedInputStream(part.getInputStream(), TAILLE_TAMPON);
            sortie = new BufferedOutputStream(new FileOutputStream(new File(chemin + nomFichier)), TAILLE_TAMPON);

            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur;

            while ((longueur = entree.read(tampon)) > 0) 
            {
                sortie.write(tampon, 0, longueur);
            }
        } 
        // 'gestion' des erreur 
        catch (Exception e)
		{
			System.out.println("erreur dans l'ecriture du fichier chargement "+chemin+"\\"+ nomFichier);
			System.out.println(e);
		}
        
        finally 
        {
            try 
            {
                sortie.close();
            } catch (IOException ignore) {
            	System.out.println("erreur dans l'ignore du fichier sotie "+chemin+"\\"+ nomFichier);
            	System.out.println(ignore);
            }
            catch (Exception e)
    		{
    			System.out.println("erreur dans l'ecriture du fichier chargement "+chemin+"\\"+ nomFichier);
    			System.out.println(e);
    		}
            
            try 
            {
                entree.close();
            } 
            catch (Exception e) 
            {
            	System.out.println("erreur dans l'ignore du fichier entree "+chemin+"\\"+ nomFichier);
            	System.out.println(e);
            }
        }
    }
    
    
   
    /**
     * recuperation du nom de fichier.
     * 
     * !! bout de code récupérer ne pas toucher sans un minimum de compréhension 
     * @param part
     * @return
     */
    public static String getNomFichier( Part part ) {
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            if ( contentDisposition.trim().startsWith( "filename" ) ) {
                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        }
        return null;
    }   
}
