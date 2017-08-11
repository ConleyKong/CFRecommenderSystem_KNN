package com.billsoft.util;

import java.util.HashMap;

public class ItemList {
	private int id;
	private long totalAtt1;
	private long totalAtt2;
	private double avg1;
	private double avg2;
	private int size;
	private HashMap<Integer,int[]> itemMap;
	
	public ItemList(){
		itemMap = new HashMap<Integer,int[]>();
	}
	
	public ItemList(int id){
		this.id = id;
		itemMap = new HashMap<Integer,int[]>();
	}
	
	public boolean addItem(int itemID,int attr1,int attr2){
		int temp[] = new int[]{attr1,attr2};
		itemMap.put(itemID, temp);
		size += 1;
		totalAtt1 += (long)attr1;
		totalAtt2 += (long)attr2;
		return true;
	}
	
	public boolean updateAvg(){
		double a1 = (double)totalAtt1/size;
		double a2 = (double)totalAtt2/size;
		this.avg1 = a1;
		this.avg2 = a2;
		return true;
	}
	
//==============================================================	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public long getTotalAtt1() {
		return totalAtt1;
	}

	public void setTotalAtt1(long totalAtt1) {
		this.totalAtt1 = totalAtt1;
	}

	public long getTotalAtt2() {
		return totalAtt2;
	}

	public void setTotalAtt2(long totalAtt2) {
		this.totalAtt2 = totalAtt2;
	}

	public double getAvg1() {
		return avg1;
	}

	public void setAvg1(double avg1) {
		this.avg1 = avg1;
	}

	public double getAvg2() {
		return avg2;
	}

	public void setAvg2(double avg2) {
		this.avg2 = avg2;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public HashMap<Integer, int[]> getItemMap() {
		return itemMap;
	}

	public void setItemMap(HashMap<Integer, int[]> itemMap) {
		this.itemMap = itemMap;
	}

		
}
