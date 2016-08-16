	/**
	 * @author xavier
	 *
	 */

package com.subtitlor.dao;

	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.sql.PreparedStatement;
	import java.util.ArrayList;


import com.subtitlor.utilities.TraduitSrtPage;


/**
 *  gestion de la base de donnée mysql.
 * derive de TraduitSrtDao
 * 
 *  structure de 
 *    String [3] parameter = {
 *                 url,
 *                 username,
 *                 password};
 *   qui permet de faire la connexion a la base de donnée
 *  
 * 
**/

	
	public class TraduitSrtDaoMysql implements TraduitSrtDao {
	 
		// * variable initialiser a la creation de l'object par parameter[3]
	    private String url;
	    private String username;
	    private String password;
	    // *
	    
	    private Connection connection; // connexion a la base
	    
	    
  /**
   * constructeur
   * 	
   * @param parametere     
   */
	    TraduitSrtDaoMysql (String [] parameter ) {
	    	// on initialise les variables.
	    	setParameter (parameter);	
	    	
	    	// on vérifie que la librairie est bien présente
	    	try {
	    		Class.forName("com.mysql.jdbc.Driver");
	    	} 
	    	catch (ClassNotFoundException e) {
	    		System.out.println("erreur au chargement de la librairie mysql");
	    		System.out.println(e);        	
	    	}

	    	// on se connecte a la base de donnée
	    	try {
	    		connection=DriverManager.getConnection(url, username, password);
	    	} 
	    	catch (SQLException e) {
	    		System.out.println("erreur lors de la connexion a la base de donnée "+ url);
	    		System.out.println(e);  
	    	}	
	    }

    
	    /**
	     * add (TraduiSrt)
	     * ajoute une structure TraduiSrt (page) a la base de donnée
	     *  
	     * @param lignes
	     *   structure TraduiSrt
	     * 
	     */
	    
	    
	    protected void add(TraduitSrtPage page) {
	    	
	    	try {
	    		
	    		PreparedStatement preparedStatement; 
	    		
	    		// Insertion dans la base de la période (time) d'affichage
	    		preparedStatement = connection.prepareStatement("INSERT INTO times(numLigne, time) VALUES(?, ?);");
	    		preparedStatement.setInt(1, page.getNumLigne());
	    		preparedStatement.setString(2, page.getTime());
	    		preparedStatement.executeUpdate();


	    		// insertion dans la base des lignes original
	    		for (String ligne:page.getOriginal())
	    		{
	    			preparedStatement = connection.prepareStatement("INSERT INTO original (numLigne, text) VALUES(?, ?);");
	    			preparedStatement.setInt(1, page.getNumLigne());
	    			preparedStatement.setString(2, ligne);
	    			preparedStatement.executeUpdate();
	    		}

	    		// insertion dans la base des lignes traduit
	    		for (String ligne:page.getTraduit())
	    		{
	    			if (!ligne.isEmpty())
	    			{	
	    				preparedStatement = connection.prepareStatement("INSERT INTO traduit(numLigne, text) VALUES(?, ?);");
	    				preparedStatement.setInt(1, page.getNumLigne());
	    				preparedStatement.setString(2, ligne);
	    				preparedStatement.executeUpdate();
	    			}
	    		}
	    	} 
	    	catch (SQLException e) {
	    		
	    		System.out.println("erreur lors de l'insersion dans la base de donnée Mysql "+ url);
	    		System.out.println(e);  
	    	}

	    }

	    
	    

	    /* (non-Javadoc)
	     * @see com.subtitlor.dao.TraduitSrtDao#write(java.util.ArrayList)
	     */
	    @Override
	    public void write(ArrayList<TraduitSrtPage> video) {
	    	
	    	// on vide la base de donnée
	    	try {
	    		PreparedStatement preparedStatement;
	    		preparedStatement = connection.prepareStatement("delete from times;");
	    		preparedStatement.executeUpdate();

	    		preparedStatement = connection.prepareStatement("delete from original;");
	    		preparedStatement.executeUpdate();

	    		preparedStatement = connection.prepareStatement("delete from traduit;");
	    		preparedStatement.executeUpdate();
	    	}
	    	catch (Exception e){
	    		System.out.println(e);
	    	}

	    	// pour chaque page je l'ajoute a la base de donnée.
	    	for (TraduitSrtPage page:video)
	    	{
	    		add(page);
	    	}

	    }


	    /* (non-Javadoc)
	     * @see com.subtitlor.dao.TraduitSrtDao#read()
	     */
	    @Override
	    public ArrayList<TraduitSrtPage> read() {
	    	ArrayList<TraduitSrtPage> resultat = new ArrayList<TraduitSrtPage>();
	    	Statement statement = null;
	    	ResultSet resultatRequette = null;

	    	try {
	    		statement = connection.createStatement();
	    		// récupère pour chaque page les temps d'affichage
	    		resultatRequette = statement.executeQuery("SELECT numLigne, time FROM times;");
	    		while (resultatRequette.next()) {
	    			int numLigne = resultatRequette.getInt("numLigne")-1;
	    			String time = resultatRequette.getString("time");
	    			resultat.add(numLigne,new TraduitSrtPage());
	    			resultat.get(numLigne).setNumLigne(numLigne+1);
	    			resultat.get(numLigne).setTime(time);
	    		}
	    		// récupère pour chaque page le text original
	    		resultatRequette = statement.executeQuery("SELECT numLigne, text FROM original;");
	    		while (resultatRequette.next()) {
	    			int numLigne = resultatRequette.getInt("numLigne")-1;
	    			String text = resultatRequette.getString("text");
	    			resultat.get(numLigne).getOriginal().add(text);
	    		}
	    		// récupère pour chaque page le text traduit
	    		resultatRequette = statement.executeQuery("SELECT numLigne, text FROM traduit;");
	    		while (resultatRequette.next()) {
	    			int numLigne = resultatRequette.getInt("numLigne")-1;
	    			String text = resultatRequette.getString("text");
	    			resultat.get(numLigne).getTraduit().add(text);
	    		}
	    	} 
	    	catch (SQLException e) {
	    		System.out.println("erreur lors de la lecture de la base "+ url);
	    		System.out.println(e);
	    	} 
	    	return resultat;
	    }

	    @Override
	    public void setParameter(String[] parameter) {
	    	// TODO Auto-generated method stub
	    	// il manque des verification du format des parametres
	    	url = parameter[0];
	    	username =  parameter[1];
	    	password =  parameter[2];

	    }


	    @Override
	    public String[] getParameter() {
	    	String [] result= new String[3];
	    	result[0]=url;
	    	result[1]=username;
	    	result[2]=password;
	    	return result;
	    }
	}
