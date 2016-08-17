
package com.subtitlor.utilities;

import java.util.ArrayList;
/**
 * @author xavier
 *
 */
public class TraduitSrtPage {
	private int numPage;
	private String time;
	private ArrayList<String> original;
	private ArrayList<String> traduit;
	
    public TraduitSrtPage()
	{
		original=new ArrayList<String>();
		traduit=new ArrayList<String>();
		time="";
	}

	public int getNumPage() {
		return numPage;
	}
	public void setNumPage(int numPage) {
		this.numPage = numPage;
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
	
	
}
