package com.billsoft.predictor;

import java.util.PriorityQueue;

import com.billsoft.util.KNNUserVector;


public interface Predictor {

	abstract double predictUser(int movieID, PriorityQueue<KNNUserVector> knn);
}
