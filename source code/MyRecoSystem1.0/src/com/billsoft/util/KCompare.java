package com.billsoft.util;

import java.util.Comparator;

public class KCompare implements Comparator<KNNUserVector>{
	
	@Override
	public int compare(KNNUserVector userVector1, KNNUserVector userVector2) {
		/**
		 * returns whether o1>o2
		 */
		if(userVector1.similarity < userVector2.similarity)
			return -1;
		else if(userVector1.similarity == userVector2.similarity)
			return 0;
		else return 1;
		
	}

}
