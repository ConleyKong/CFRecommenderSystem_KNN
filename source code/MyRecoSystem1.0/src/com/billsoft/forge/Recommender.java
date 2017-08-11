package com.billsoft.forge;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

import com.billsoft.predictor.ItemBasedPredictor;
import com.billsoft.similarity.CalSimilarity;
import com.billsoft.similarity.CosineSimilarity;
import com.billsoft.util.ItemAttributeMatrix;
import com.billsoft.util.ItemList;
import com.billsoft.util.KCompare;
import com.billsoft.util.KNNUserVector;
import com.billsoft.util.ObjectVector;
import com.billsoft.util.UserVector;


public class Recommender {
	public HashMap<Integer, ObjectVector> users = new HashMap<Integer, ObjectVector>();
	public ItemAttributeMatrix iMatrix = new ItemAttributeMatrix(); 
	public ItemList itemAttributeList = new ItemList();

	//record testing items
	public HashSet<Integer> qUserItems;
	public PrintWriter out;
	
	public int itemNum = 0;
	public int predictedNum = 0;

	/**
	 * This method reads in the trainFile and constructs the Item and User
	 * vectors.
	 * 
	 * @param trainFile
	 */
	public void constrcutTrain(String trainFile) {
		Scanner scanner = null;
		int itemID = -1;
		int userID = -1;
		int inum = -1;
		byte score = -1;
		try {
			scanner = new Scanner(new File(trainFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		while(scanner.hasNext()){
			String headInfo = scanner.nextLine();// get the head.
			int ti = headInfo.lastIndexOf('|');
			String uStr = headInfo.substring(0, ti);// userID 
			String nStr = headInfo.substring(ti+1);//num of items
			userID = Integer.parseInt(uStr);
			
			UserVector uv = new UserVector(userID); 
			int totalScore = 0;
			inum = Integer.parseInt(nStr);//the num of user's recommended
			System.out.println("---------------------------------");
			System.out.println("用户Id："+userID+"评论商品数："+inum);
			for(int i = 0;i<inum;i++){
				//get comment scores line by line
				itemID = scanner.nextInt();//function split by line break
				score = (byte)scanner.nextInt();
				if(score!=0){
					totalScore += score;
					uv.addItemRecord(itemID, score);
					System.out.println("物品Id："+itemID+"评分："+score);
				}
				
			}	
			users.put(userID, uv);
			scanner.nextLine();
		}
		scanner.close();
	}

	public void constructItemAttributeMatrix(String attributeFile) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(attributeFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		while(scanner.hasNext()){
			String linedata = scanner.nextLine();
			System.out.println(linedata);
			itemNum++;
			int i = linedata.indexOf('|');
			int ii = linedata.lastIndexOf('|');
			String idSub = linedata.substring(0, i);
			String at1Sub = linedata.substring(i+1,ii);
			String at2Sub = linedata.substring(ii+1);
			int id = Integer.parseInt(idSub);
			int at1 = at1Sub.equals("None")?0:Integer.parseInt(at1Sub);
			int at2 = at2Sub.equals("None")?0:Integer.parseInt(at2Sub);
			iMatrix.addItem(id, at1, at2);
			itemAttributeList.addItem(id, at1, at2);
			
		}
		
	}
	
	public void processQuery(String testFile,String outputFile) {
		/**
		 * this method read data from testFile and write predict into outputFile
		 * the para of k means the max queue length  
		 */
		int k = 10;
		ObjectVector qUser;
		
		//first construct the query list
		Scanner tfScanner = null;
		Scanner attributeScanner = null;
		try {
			tfScanner = new Scanner(new File(testFile));
			out = new PrintWriter(
					new BufferedWriter(new FileWriter(outputFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("scanner打开错误！");
		}catch (IOException e) {
			System.out.println("写入文件打开错误！");
			e.printStackTrace();
		}
		while (true) {
			String headInfo = tfScanner.nextLine();// get the head.
			int ti = headInfo.lastIndexOf('|');
			String uStr = headInfo.substring(0, ti);// userID 
			String nStr = headInfo.substring(ti+1);//num of items
			int queryUserID = Integer.parseInt(uStr);		//userID use for collaboration filter
			int num = Integer.parseInt(nStr);
			qUser = new UserVector(queryUserID);
			qUserItems = new HashSet<Integer>();
			for(int i =0;i<num;i++){
				int iID = tfScanner.nextInt();
				qUserItems.add(iID);
			}
			// make a prediction of this information group 

			out.println(queryUserID+"|"+num);
			out.flush();
			System.out.println("输出："+queryUserID+"|"+num);
			
			ItemBasedPredictor predictor = new ItemBasedPredictor();
			CalSimilarity cal = new CosineSimilarity();
			
			PriorityQueue<KNNUserVector> knn = knnSearch(qUser,cal);
			
			//get the score for each item
			Iterator<Integer> it = qUserItems.iterator();
			HashMap<Integer,Byte> qUserItems = users.get(queryUserID).getRecords();
			HashSet<Integer> similarItems = null;
			double uAvg = users.get(queryUserID).getAvgScore();
			while(it.hasNext()){
				int iID = (Integer)it.next();
				int[] ats = itemAttributeList.getItemMap().get(iID);
				if(ats!=null){
					int at1 = ats[0];
					int at2 = ats[1];
					similarItems = iMatrix.getSimilarItems(at1, at2);
				}
				int score = (int)predictor.predictUser(iID, knn,qUserItems,similarItems,uAvg);
				out.println(iID + " " + score);
				predictedNum++;
				System.out.println("输出："+iID + " " + score);
				out.flush();
			}
			//next user's score predict
			if(tfScanner.hasNext()){
				tfScanner.nextLine();
			}else{
				break;
			}
		}
		System.out.println("已经完成评分，请查看结果文件！");
		out.close();
		tfScanner.close();
	}
	
	private PriorityQueue<KNNUserVector> knnSearch(ObjectVector qUser, CalSimilarity cal) {
		/**
		 * this method returns an knnPriorityQueue which contains an new created knnuservector
		 * the result knnPriorityQueue contains the most similarity items between
		 */
		
		int k = 10;
		double simi = 0.0;
		
		PriorityQueue<KNNUserVector> knnPQ = new PriorityQueue<KNNUserVector>(k,new KCompare());
		Iterator<Integer> it = users.keySet().iterator();
		while(it.hasNext()){
			int i = (Integer)it.next();
			ObjectVector tempUser = users.get(i);
			simi = cal.calSimilarity(qUser, tempUser);
			knnPQ.add(new KNNUserVector(tempUser, simi));
			if (knnPQ.size() > k) {
				knnPQ.poll();
				// remove the least similarity user's item
			}
		}
		return knnPQ;
	}
	
	
	
}