package com.subtitlor.dao;

	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.sql.PreparedStatement;
	import java.util.ArrayList;


import com.subtitlor.utilities.TraduitSrt;

	public class TraduitSrtDaoMysql implements TraduitSrtDao {
	    private Connection connection;
	    private String url = "jdbc:mysql://localhost:3306/subtitlor";
	    private String username = "xavier";
	    private String password = "challans";
	    
	    TraduitSrtDaoMysql (String [] parametere ) {
	    
	    setParametere (parametere);	
	    
	    try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
        	System.out.println(e);        	
        }
	    
	    try {
			connection=DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e);  
			e.printStackTrace();
		}	
	    }
	    
	   
		public void add(TraduitSrt lignes) {
	        PreparedStatement preparedStatement = null;

	        try {
	                  	
	        	preparedStatement = connection.prepareStatement("INSERT INTO times(numLigne, time) VALUES(?, ?);");
	            preparedStatement.setInt(1, lignes.getNumLigne());
	            preparedStatement.setString(2, lignes.getTime());
	            preparedStatement.executeUpdate();
	            
	                  
	            
	            for (String ligne:lignes.getOriginal())
	            {
	            	preparedStatement = connection.prepareStatement("INSERT INTO original (numLigne, text) VALUES(?, ?);");
	            	preparedStatement.setInt(1, lignes.getNumLigne());
	            	preparedStatement.setString(2, ligne);
	            	preparedStatement.executeUpdate();
	            }
	            
	            for (String ligne:lignes.getTraduit())
	            {
	            	if (!ligne.isEmpty())
	            	{	
	            	preparedStatement = connection.prepareStatement("INSERT INTO traduit(numLigne, text) VALUES(?, ?);");
	            	preparedStatement.setInt(1, lignes.getNumLigne());
	            	preparedStatement.setString(2, ligne);
	            	preparedStatement.executeUpdate();
	            	}
	            }
	            
	            
	            //System.out.println(lignes.getNumLigne());
	            
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	    }
	    
	    @Override
		public void write(ArrayList<TraduitSrt> lignes) {
	    	PreparedStatement preparedStatement = null;
	    	 try {
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
	    	
	    	
	    	for (TraduitSrt ligne:lignes)
			{
				add(ligne);
			}
			
		}
	    
	    
		@Override
		public ArrayList<TraduitSrt> read() {
			ArrayList<TraduitSrt> resultat = new ArrayList<TraduitSrt>();
	        Statement statement = null;
	        ResultSet resultatRequette = null;
	        
	        try {
	            statement = connection.createStatement();

	            // Ex�cution de la requ�te
	            resultatRequette = statement.executeQuery("SELECT numLigne, time FROM times;");
	            while (resultatRequette.next()) {
	                int numLigne = resultatRequette.getInt("numLigne")-1;
	                String time = resultatRequette.getString("time");
	                resultat.add(numLigne,new TraduitSrt());
					resultat.get(numLigne).setNumLigne(numLigne+1);
					resultat.get(numLigne).setTime(time);
	            }
	            resultatRequette = statement.executeQuery("SELECT numLigne, text FROM original;");
	            while (resultatRequette.next()) {
	                int numLigne = resultatRequette.getInt("numLigne")-1;
	                String text = resultatRequette.getString("text");
	                resultat.get(numLigne).getOriginal().add(text);
	            }
	            resultatRequette = statement.executeQuery("SELECT numLigne, text FROM traduit;");
	            while (resultatRequette.next()) {
	                int numLigne = resultatRequette.getInt("numLigne")-1;
	                String text = resultatRequette.getString("text");
	                resultat.get(numLigne).getTraduit().add(text);
	            }
	        } catch (SQLException e) {
	        } 
			return resultat;
		}
		
		@Override
		public void setParametere(String[] Name) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public String[] getParametere() {
			// TODO Auto-generated method stub
			return null;
		}
	}
