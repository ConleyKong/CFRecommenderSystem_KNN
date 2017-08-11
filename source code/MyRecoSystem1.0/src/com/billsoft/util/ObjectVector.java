package com.billsoft.util;

import java.util.HashMap;

public interface ObjectVector {
	public double getAvgScore();

	public void setAvgScore(double avgScore);

	public int getSize();

	public void setSize(int size);
	
	//didn't notice the record operations which is very important but various type
	public HashMap<Integer, Byte> getRecords();
	
	public void setRecords(HashMap<Integer, Byte> records);
}
