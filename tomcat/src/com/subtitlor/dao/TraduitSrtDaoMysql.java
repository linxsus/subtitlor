package com.subtitlor.dao;

	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.sql.PreparedStatement;
	import java.util.ArrayList;
	import java.util.List;

import com.subtitlor.utilities.TraduitSrt;

	public class TraduitSrtDaoMysql implements TraduitSrtDao {
	    private Connection connexion;
	    
	    TraduitSrtDaoMysql (DaoFactorySql daoFactory) {
	    
	        try{
	        connexion = daoFactory.getConnection();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	    }
	    public void add(TraduitSrt lignes) {
	        PreparedStatement preparedStatement = null;

	        try {
	                  	
	        	preparedStatement = connexion.prepareStatement("INSERT INTO times(numLigne, time) VALUES(?, ?);");
	            preparedStatement.setInt(1, lignes.getNumLigne());
	            preparedStatement.setString(2, lignes.getTime());
	            preparedStatement.executeUpdate();
	            
	            for (String ligne:lignes.getOriginal())
	            {
	            	preparedStatement = connexion.prepareStatement("INSERT INTO original (numLigne, text) VALUES(?, ?);");
	            	preparedStatement.setInt(1, lignes.getNumLigne());
	            	preparedStatement.setString(2, ligne);
	            	preparedStatement.executeUpdate();
	            }
	            
	            for (String ligne:lignes.getTraduit())
	            {
	            	if (!ligne.isEmpty())
	            	{	
	            	preparedStatement = connexion.prepareStatement("INSERT INTO traduit(numLigne, text) VALUES(?, ?);");
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
	    	preparedStatement = connexion.prepareStatement("delete from times;");
        	preparedStatement.executeUpdate();
            
        	preparedStatement = connexion.prepareStatement("delete from original;");
        	preparedStatement.executeUpdate();
            
        	preparedStatement = connexion.prepareStatement("delete from traduit;");
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
	            statement = connexion.createStatement();

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
	}
