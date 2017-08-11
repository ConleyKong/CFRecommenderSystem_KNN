package com.billsoft.similarity;

import com.billsoft.util.ObjectVector;


public interface CalSimilarity {
   
	public abstract double calSimilarity(ObjectVector query, ObjectVector uv);
}
