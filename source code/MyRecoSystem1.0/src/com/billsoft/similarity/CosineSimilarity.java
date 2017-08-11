package com.billsoft.similarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.billsoft.util.ObjectVector;


public class CosineSimilarity implements CalSimilarity{
private boolean useAvg = false;
	
	@Override
	public double calSimilarity(ObjectVector users, ObjectVector others) {
		HashMap<Integer, Byte> userItems =  users.getRecords();
		HashMap<Integer, Byte> otherItems = others.getRecords();
		
		double avg1 = (useAvg?users.getAvgScore():50);
		double avg2 = (useAvg?others.getAvgScore():50);
		
		Set<Integer> all = new HashSet<Integer>();
		//this step get the intersection of these key sets
			all.addAll(userItems.keySet());
			all.retainAll(otherItems.keySet());
		
		double simiTotal = 0.0;
		double o1 = 0;
		double o2 = 0;
		
		for (int key : all) {
			double x1 = 0;
			double x2 = 0;
			x1 = userItems.get(key) - avg1;
			x2 = otherItems.get(key) -avg2;
			simiTotal += x1 * x2;
		}
		for(int score: userItems.values()){
			o1 +=(score-avg1) * (score-avg1);
		}
		for(int score: otherItems.values()){
			o2 +=(score-avg2) * (score-avg2);
		}
		
		if(o1 ==0.0 || o2 ==0.0){
			return 0;
		}
		
		return simiTotal / (Math.sqrt(o2) * Math.sqrt(o1));
	}

	public boolean isUseAvg() {
		return useAvg;
	}


	public void setUseAvg(boolean useAvg) {
		this.useAvg = useAvg;
	}


}
