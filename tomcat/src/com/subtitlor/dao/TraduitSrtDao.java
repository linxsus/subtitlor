package com.subtitlor.dao;

import java.util.ArrayList;

import com.subtitlor.utilities.TraduitSrt;

public interface TraduitSrtDao {
	public void write(ArrayList<TraduitSrt> lignes);
	public ArrayList<TraduitSrt> read();
}
