package com.billsoft.predictor;

import java.util.PriorityQueue;

import com.billsoft.util.KNNUserVector;


public class MeanPredictor implements Predictor {

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

}
