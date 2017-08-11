package com.billsoft.util;

import java.util.HashMap;
import java.util.HashSet;

public class ItemAttributeMatrix {
	/**
	 * @author Conley
	 * in this class we will generate a matrix which X-aixs will indicate 
	 * the first attribute and the Y-aixs will indicate the second attribute
	 * the element will put the item's id
	 * 1)if two item have the same attribute value ,then add them into a hashMap
	 * 2)when get element from this class ,we first find if there has an element in
	 * this matrix ,if has then check if hashMap contains another same values;if not,
	 * we return false;
	 */
	private final int MAXSIZE = 6300;
	private final int SCALE = 100;
	private final int LEVEL = 3;//search radius
	private int[][] itemMatrix = null;
	private HashMap<Integer,HashSet<Integer>> sameValueItemTable = null;
	
	public ItemAttributeMatrix(){//construct function
		itemMatrix = new int[MAXSIZE][MAXSIZE];
		sameValueItemTable = new HashMap<Integer,HashSet<Integer>>();
		for(int i = 0;i<MAXSIZE;i++){//initializing the itemMatrix
			for(int j = 0;j<MAXSIZE;j++){
				itemMatrix[i][j] = 0;
			}
		}
	}
	
	public boolean addItem(int itemID,int at1,int at2){
		// reduce the scale
		at1 = at1/SCALE; 
		at2 = at2/SCALE;
		if(itemMatrix[at1][at2]==0){
			itemMatrix[at1][at2] = itemID;
			return true;
		}else{
			int key = itemMatrix[at1][at2];
			if(sameValueItemTable.containsKey(key)){
				sameValueItemTable.get(key).add(itemID);
				return true;
			}else{
				HashSet<Integer> value = new HashSet<Integer>();
				value.add(itemID);
				sameValueItemTable.put(key, value);
				return true;
			}
		}
	}
	
	public HashSet<Integer> getSimilarItems(int at1,int at2){
		//this method return all the similar Items' id as a hashSet
		//if the hashmap has some value then output
		//if not , search the 3 level around this pixels
		//the search ended when the similar element reached 10 or finished the 3
		//level search
		at1 = at1/SCALE;
		at2 = at2/SCALE;
		HashSet<Integer> result = new HashSet<Integer>();
		int iid = itemMatrix[at1][at2];
		if(iid != 0){
			result.add(iid);
			HashSet<Integer> temp = this.sameValueItemTable.get(iid);
			if(temp!=null){
				result.addAll(temp);
			}
		}
		//search around this element
		for(int i=1;i<LEVEL;i++){
			//up
			if(at1-i>0&&at1+i<MAXSIZE&&at2+i<MAXSIZE){
				for(int j=at1-i;j<at1+i;j++){
					int _iid = itemMatrix[j][at2+i];
					if(_iid != 0){
						result.add(_iid);
						HashSet<Integer> temp = sameValueItemTable.get(_iid);
						if(temp!=null){
							result.addAll(temp);
						}
					}
				}
			}	
			//down
			if(at1-i>0&&at1+i<MAXSIZE&&at2-i>0){
				for(int j=at1-i;j<at1+i;j++){
					int _iid = itemMatrix[j][at2-i];
					if(_iid != 0){
						result.add(_iid);
						HashSet<Integer> temp = sameValueItemTable.get(_iid);
						if(temp!=null){
							result.addAll(temp);
						}
					}
				}
			}
			//left
			if(at2-i>0&&at2+i<MAXSIZE&&at1-i>0){
				for(int j=at2-i;j<at2+i;j++){
					int _iid = itemMatrix[at1-i][j];
					if(_iid != 0){
						result.add(_iid);
						HashSet<Integer> temp = sameValueItemTable.get(_iid);
						if(temp!=null){
							result.addAll(temp);
						}
						
					}
				}
			}
			//right
			if(at2-i>0&&at2+i<MAXSIZE&&at1+i<MAXSIZE){
				for(int j=at2-i;j<at2+i;j++){
					int _iid = itemMatrix[at1+i][j];
					if(_iid != 0){
						result.add(_iid);
						HashSet<Integer> temp = sameValueItemTable.get(_iid);
						if(temp!=null){
							result.addAll(temp);
						}
					}
				}
			}						
		}
		return result;
	}
}	
