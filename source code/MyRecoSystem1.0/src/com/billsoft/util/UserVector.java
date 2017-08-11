package com.billsoft.util;

import java.util.HashMap;
import java.util.Iterator;

public class UserVector implements ObjectVector {
	/**
	 * These are meta information for a User Vector. Store the score as byte to
	 * save the space.
	 */
	private double avgScore = 0;
	private int size = 0;
	private int userID = 0;
	private int totalScore = 0;
	private boolean needUpdate = true; 

	HashMap<Integer, Byte> itemRecords = null;

	public UserVector() {
		this.itemRecords = new HashMap<Integer, Byte>();
	}

	public UserVector(int id) {
		this.itemRecords = new HashMap<Integer, Byte>();
		this.userID = id;
	}
	
	public UserVector(int userID, HashMap<Integer, Byte> items) {
		super();
		this.userID = userID;
		this.itemRecords = items;
		this.size += items.size();
		Iterator it = items.keySet().iterator();
		while(it.hasNext()){
			int id = (Integer)it.next();
			totalScore += items.get(id);
		}
		avgScore = totalScore/size;
		needUpdate = false;
	}

	public void addItemRecord(int itemID, byte score) {
		this.itemRecords.put(itemID, score);
		this.size++;
		this.totalScore += score;
		needUpdate = true;
	}

	
	//getters and setters 
	public double getAvgScore() {
		if(needUpdate){
			if(size==0){
				return 0;
			}
			this.avgScore = totalScore/size;
			needUpdate = false;
		}
		return avgScore;
	}

	public void setAvgScore(double avgScore) {
		this.avgScore = avgScore;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public HashMap<Integer, Byte> getItemRecords() {
		return itemRecords;
	}

	public void setItemRecords(HashMap<Integer, Byte> itemRecords) {
		this.itemRecords = itemRecords;
		this.size += itemRecords.size();
		Iterator it = itemRecords.keySet().iterator();
		while(it.hasNext()){
			int id = (Integer)it.next();
			totalScore += itemRecords.get(id);
		}
		avgScore = totalScore/size;
		needUpdate = false;
	}

	@Override
	public HashMap<Integer, Byte> getRecords() {
		return itemRecords;
	}

	@Override
	public void setRecords(HashMap<Integer, Byte> records) {
		this.itemRecords = records;
	}
	
	
}
