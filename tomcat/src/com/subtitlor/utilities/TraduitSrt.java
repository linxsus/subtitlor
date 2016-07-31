
package com.subtitlor.utilities;

import java.util.ArrayList;
/**
 * @author xavier
 *
 */
public class TraduitSrt {

	public int getNumLigne() {
		return numLigne;
	}
	public void setNumLigne(int numLigne) {
		this.numLigne = numLigne;
	}
	public ArrayList<String> getOriginal() {
		return original;
	}
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	public void setOriginal(ArrayList<String> original) {
		this.original = original;
	}
	public ArrayList<String> getTraduit() {
		return traduit;
	}
	public void setTraduit(ArrayList<String> traduit) {
		this.traduit = traduit;
	}
	private int numLigne;
	private String time;
	private ArrayList<String> original;
	private ArrayList<String> traduit;
	
    public TraduitSrt()
	{
		original=new ArrayList<String>();
		traduit=new ArrayList<String>();
		time="";
	}
	
}
