package com.billsoft.util;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Item Vector stores the records from different users, the item ID and its
 * average score. 
 * It does not store the whole sum of scores since the movies are sequentially
 *
 */
public class ItemVector implements ObjectVector {

	private int itemID;
	private double avgScore;
	private int size;
	private HashMap<Integer, Byte> records = new HashMap<Integer, Byte>();
	private int totalScore = 0;
	private boolean needUpdate = true;		//whether need to update avgscore

	public ItemVector() {
		super();
	}
	
	public ItemVector(int itemID, HashMap<Integer, Byte> users) {
		super();
		this.itemID = itemID;
		this.records = users;
		this.size += users.size();
		Iterator it = users.keySet().iterator();
		while(it.hasNext()){
			int id = (Integer)it.next();
			totalScore += users.get(id);
		}
		avgScore = totalScore/size;
		needUpdate = false;
	}

	public ItemVector(int itemID, double avgScore,
			HashMap<Integer, Byte> users) {
		super();
		this.itemID = itemID;
		int size = users.size();
		this.totalScore += avgScore*size;
		this.size += size;
		this.records = users;
		this.avgScore = totalScore/size;
		needUpdate = false;
	}

	public void addUserRecord(int userId, byte score) {
		this.records.put(userId, score);
		this.totalScore += score;
		this.size ++;
		this.needUpdate = true;
	}
	


	@Override
	public double getAvgScore() {
		if(needUpdate){
			if(size==0){
				return 0;
			}
			this.avgScore = totalScore/size;
		}
		return avgScore;
	}

	@Override
	public void setAvgScore(double avgScore) {
		this.avgScore = avgScore;
	}

	@Override
	public int getSize() {
		return this.size;
	}

	@Override
	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public HashMap<Integer, Byte> getRecords() {
		return records;
	}

	public void setRecords(HashMap<Integer, Byte> records) {
		this.records = records;
	}
	
	public int getItemID() {
		return itemID;
	}

	public void setItemID(int movieID) {
		this.itemID = movieID;
	}

}
