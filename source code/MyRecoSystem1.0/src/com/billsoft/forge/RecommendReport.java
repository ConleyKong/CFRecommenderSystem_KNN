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
import java.util.Scanner;

import com.billsoft.util.ItemList;
import com.billsoft.util.UserVector;


public class RecommendReport {
	public UserVector realUser = new UserVector();
	public UserVector resultUser = new UserVector();
	public ItemList itemAttributeList = new ItemList();
	private static int predictedNum = 0;
		
	//record testing items
	public HashSet<Integer> qUserItems;
	public PrintWriter out;
		
	private static String dataroot = "test/";

	public static void main(String[] args) {
		String trainFile = dataroot + "train.txt";
		String testFile = dataroot + "test.txt";
		String attributeFile = dataroot + "itemAttribute.txt";
		String resultFile = dataroot + "result.txt";
		String realFile = dataroot+"real.txt";
		String reportFile = "report.txt";
		
		Recommender recom = new Recommender();
		long trainStartTime = System.currentTimeMillis();
		recom.constrcutTrain(trainFile);
		recom.constructItemAttributeMatrix(attributeFile);
		long trainEndTime = System.currentTimeMillis();
		System.out.println("-------------完成属性文件的读入-------");
		System.out.println("正在进行评分工作：-------");
		recom.processQuery(testFile,resultFile);
		System.out.println("正在统计程序信息，请稍后：-------");
//		the following method will count some report statics
		/**
		 * report file need contain the contents below:
		 * 1.user number
		 * 2.items number
		 * 3.scored items number
		 * 4.training time
		 * 5.RMSE
		 * 6.memory consumption
		 */
		PrintWriter outReporter = null;
		//output the result into report.txt
		try {
			outReporter = new PrintWriter(new BufferedWriter(new FileWriter(reportFile)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long trainingTime = trainEndTime - trainStartTime;
		int userNum = recom.users.size();
		int itemNum = recom.itemNum;
		int predictedItemNum = recom.predictedNum;
		double rmse = 0.0;
		outReporter.println("1.user number：" + userNum);
		outReporter.println("2.itmes number: " + itemNum);
		outReporter.println("3.scored items number: " + predictedItemNum);
		outReporter.println("4.training time: "+trainingTime+" ms");
		outReporter.flush();
		RecommendReport rr = new RecommendReport();
		rmse = rr.calRMSE(realFile,resultFile);		
		System.out.println("计算的RMSE的值为："+rmse);
		outReporter.println("5.RMSE(Root Mean Square Error) value: "+rmse);
		outReporter.close();
		System.out.println("运行统计信息已经输出到report.txt文件中，请注意查看！");
	}

	public double calRMSE(String realFile,String resultFile){
			//input the realFile and resultFile then calculate the RMSE
			//first read in the real file and result file,then calculate the rmse
				Scanner resultScanner = null;
				Scanner realScanner = null;
				int realItemId = 0;
				int resultItemId = 0;
				Byte realScore = 0;
				Byte resultScore = 0;
				int counter = 0;
				double sumSqure = 0.0;
				
				try {
					resultScanner = new Scanner(new File(resultFile));
					realScanner = new Scanner(new File(realFile));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				//read in all the data into realUser and resultUser
				while(resultScanner.hasNext()){
					String headInfo = resultScanner.nextLine();
					String temp = realScanner.nextLine();
					System.out.println("resultScanner headInfo:"+headInfo);
					System.out.println("realScanner temp :"+temp);
					
					int num = Integer.parseInt(headInfo.substring(headInfo.indexOf('|')+1));
					System.out.println("num:"+num);
					for(int i = 0;i<num;i++){
//						System.out.println("realScanner: "+realScanner.nextInt());
//						System.out.println("resultScanner: "+resultScanner.nextInt());
						
						realItemId = realScanner.nextInt();
						realScore = realScanner.nextByte();
						realUser.addItemRecord(realItemId, realScore);
						
						resultItemId = resultScanner.nextInt();
						resultScore = resultScanner.nextByte();
						resultUser.addItemRecord(resultItemId, resultScore);
//						System.out.println("realScore:"+realScore);
//						System.out.println("resultScore"+resultScore);
					}
					//then compare the same items' score
					HashMap<Integer,Byte> realItems = realUser.getItemRecords();
					HashMap<Integer,Byte> resultItems = resultUser.getItemRecords();
					Iterator it = realItems.keySet().iterator();
					while(it.hasNext()){
						int iid = (Integer)it.next();
						if(resultItems.containsKey(iid)){
							realScore = realItems.get(iid);
							resultScore = resultItems.get(iid);
//							System.out.println("Calculating:"+realScore+"-"+resultScore);
							sumSqure += (realScore-resultScore)*(realScore-resultScore);
							counter ++;
						}
					}
					
					resultScanner.nextLine();
					realScanner.nextLine();
				}
				realScanner.close();
				resultScanner.close();
				System.out.println("counter value: "+counter);
				double rmse = Math.sqrt((double)sumSqure/counter);
				return rmse;
	}
		
}