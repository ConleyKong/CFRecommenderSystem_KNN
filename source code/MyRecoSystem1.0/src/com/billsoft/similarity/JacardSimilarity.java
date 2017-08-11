package com.billsoft.similarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.billsoft.util.ObjectVector;


public class JacardSimilarity implements CalSimilarity{

     
	@Override
	public double calSimilarity(ObjectVector userVector, ObjectVector otherVector) {
		/**
		 * this method return the similarity between userVector and otherVector using jacardSimilarity
		 */
		HashMap<Integer, Byte> userRecords =  userVector.getRecords();
		HashMap<Integer, Byte> otherRecords = otherVector.getRecords();
		
		Set<Integer> s1 = userRecords.keySet();
		Set<Integer> s2 = otherRecords.keySet();
		Set<Integer> unionAll = new HashSet<Integer>();
		Set<Integer> intersectionAll = new HashSet<Integer>();
		unionAll.addAll(s1);
		unionAll.addAll(s2);
		
		intersectionAll.addAll(s1);
		intersectionAll.retainAll(s2);

		int isize = intersectionAll.size();
		int usize = unionAll.size();
		double jacardSim = (double)isize/usize;
		return jacardSim;
	}
	
}
