package com.billsoft.predictor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

import com.billsoft.util.KNNUserVector;


public class ItemBasedPredictor implements Predictor {

	@Override
	public double predictUser(int itemID, PriorityQueue<KNNUserVector> knn) {
		/**
		 * in this method, knn contains all the most similar users' item vector
		 * which aims to get the similarity between itemID and user's items
		 * this method returns the average score calculated  
		 */
		double score = 0.0;
		double num = 0.0;

		for (KNNUserVector similarUser : knn) {
			double itemScore = 0;
			if (similarUser.getVector().getRecords().containsKey(itemID)) {
				itemScore = similarUser.getVector().getRecords().get(itemID);
			} else {
				itemScore = similarUser.getVector().getAvgScore();
			}
			score += itemScore;
			num += 1;
		}
		double finalscore = (double)score/num;
		return finalscore;
	}
	
	public double predictUser(int itemID, PriorityQueue<KNNUserVector> knn
			,HashMap<Integer,Byte> userItems,HashSet<Integer> groupedItems){
		/**
		 * this method predict score based on both knn and itemgroup 
		 * the para list:
		 * itemID 		:the item id we want to predict
		 * knn	  		:the priority queue which contains all the most similar user's items vector
		 * groupedItems	:all the grouped items 
		 */
		boolean hasPredicted = false;
		double score = 0.0;
		int num = 0;
		//all the below paras is under the beneath assumption:user has remark the similar items
		double alpha = 0.4;//parameter for similar user's score
		double beta = 0.6;//parameter for user's similar remarks on similar used items

		for (KNNUserVector similarUser : knn) {			//相似用户购买过该物品
			double itemScore = 0;
			if (similarUser.getVector().getRecords().containsKey(itemID)) {
				itemScore = similarUser.getVector().getRecords().get(itemID);
				score += itemScore;
				num ++;
			}
		}
		if(num!=0){
			hasPredicted = true;
			score = (double)score/num;
		}
		//search if this user have bought the similar items
		double similarItemScore = 0.0;
		int snum = 0;
		Iterator<Integer> it = groupedItems.iterator();
		while (it.hasNext()) {
			int iID = (Integer) it.next();
			if (userItems.containsKey(iID)) {
				similarItemScore += userItems.get(iID);
				snum++;
			}
		}
		if (snum != 0) {// this means has some similar items like what we
			hasPredicted = true;
			// are finding
			similarItemScore = similarItemScore / snum;
			// recalculate the score
			score = alpha * score + beta * similarItemScore;
		}
			
			return score;
	}

	public double predictUser(int itemID, PriorityQueue<KNNUserVector> knn
			,HashMap<Integer,Byte> userItems,HashSet<Integer> similarItems,double uAvg){
		/**
		 * need parameters like :user's average score and item's average score
		 * itemID implies the query item's id
		 * knn 	  implies the similar users
		 * userItems implies the user's items have bought
		 * similarItems implies the similar items with the query item
		 * uAvg implies user's avg score
		 */
		
		boolean hasPredicted = false;
		double score = 0.0;
		int num = 0;
		//all the below paras is under the beneath assumption:user has remark the similar items
		double alpha = 0.4;//parameter for similar user's score
		double beta = 0.6;//parameter for user's similar remarks on similar used items

		for (KNNUserVector similarUser : knn) {	//if similar user has bought this item
			double itemScore = 0;
			if (similarUser.getVector().getRecords().containsKey(itemID)) {
				itemScore = similarUser.getVector().getRecords().get(itemID);
				score += itemScore;
				num ++;
			}
		}
		if(num!=0){
			hasPredicted = true;
			score = (double)score/num;
		}
		//search if user have bought the similar items
		double similarItemScore = 0.0;
		int snum = 0;
		if(similarItems!=null){
			Iterator<Integer> it = similarItems.iterator();
			while (it.hasNext()) {
				int iID = (Integer) it.next();
				if (userItems.containsKey(iID)) {
					similarItemScore += userItems.get(iID);
					snum++;
				}
			}
		}
		
		if(num!=0){
			score = score/num;
			if (snum != 0) {// this means has some similar items like what we
				// are finding
				score = alpha*(score)+beta*(similarItemScore/snum);
			}
		}
		
		if(score == 0.0){
			score = uAvg;
		}
		return score;
	}
	
	public double predictUser0(int itemID, PriorityQueue<KNNUserVector> knn
			,HashMap<Integer,Byte> userItems,HashSet<Integer> similarItems,double uAvg){
		/**
		 * need parameters like :user's average score and item's average score
		 * itemID implies the query item's id
		 * knn 	  implies the similar users
		 * userItems implies the user's items have bought
		 * groupedItems implies the similar items with the query items
		 * uAvg implies user's avg score
		 */
		double score = 0.0;
		double similarItemScore = 0.0;
		double alpha = 0.6;		//item's avg score weight
		double beta = 0.4;		//user's avg score weight
		//all the below paras is under the beneath assumption:user has remark the similar items

		//search if user have bought the similar items
		int num = 0;
		int snum = 0;
		Iterator<Integer> it = similarItems.iterator();
		while (it.hasNext()) {
			int iID = (Integer) it.next();		//similar items
			if (userItems.containsKey(iID)) {
				score += userItems.get(iID);
				snum++;
			}
			for (KNNUserVector similarUser : knn) {	//if similar user has bought this item
				HashMap<Integer,Byte> ui = similarUser.getVector().getRecords(); 
				if (ui.containsKey(iID)) {
					Byte temp = ui.get(itemID);
					if(temp!=null){
						similarItemScore += (double)temp;
						snum ++;
					}
				}
			}
		}
		if(num!=0){
			score = score/num;
			if (snum != 0) {// this means has some similar items like what we
				// are finding
				score = alpha*(score)+beta*(similarItemScore/snum);
			}
		}
		
		if(score == 0.0){
			score = uAvg;
		}
			return score;
	}
}
