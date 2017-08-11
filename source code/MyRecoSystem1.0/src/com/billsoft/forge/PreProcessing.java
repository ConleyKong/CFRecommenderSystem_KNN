package com.billsoft.forge;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class PreProcessing {
	/**
	 * this class used to generate test file and corresponding real file used for rmse
	 */

	public PrintWriter out;
		
	private static String dataroot = "test/";

	public static void main(String[] args) {
		String trainFile = dataroot + "train.txt";
		String testFile = dataroot + "test.txt";
		String realFile = dataroot + "real.txt";

		PreProcessing prep = new PreProcessing();
		prep.generateFiles(trainFile,testFile,realFile);
		System.out.println("文件转换完成-------");
		

	}

	private void generateFiles(String trainFile,String testFile,String realFile) {
		Scanner scanner = null;
		PrintWriter outtest = null;
		PrintWriter outreal = null;
		HashMap<Integer,Byte> records = new HashMap<Integer,Byte>();
		int userid;
		int tn;
		int rn;
		try {
			scanner = new Scanner(new File(trainFile));
			outtest = new PrintWriter(
					new BufferedWriter(new FileWriter(testFile)));
			outreal = new PrintWriter(
					new BufferedWriter(new FileWriter(realFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(scanner.hasNext()){
			String linedata = scanner.nextLine();
			System.out.println("input: "+linedata);
			int i = linedata.indexOf('|');
			userid = Integer.parseInt(linedata.substring(0, i));
			tn = Integer.parseInt(linedata.substring(i+1));
			
			int itemid;
			byte score;
			for (int j = 0; j < tn; j++) {
				itemid = scanner.nextInt();
				score = scanner.nextByte();
				if (score != 0) {
					records.put(itemid, score);
				}
				System.out.println("input: " + itemid + " " + score);
			}
			scanner.nextLine();
			// output separately into real file and test file
			rn = records.size() / 8;// the real number line output
			if (rn == 0) {
				records.clear();
				continue;
			}
			Iterator it = records.keySet().iterator();
			outtest.println(userid + "|" + rn);
			outreal.println(userid + "|" + rn);
			for (int j = 0; j < rn; j++) {
				itemid = (Integer) it.next();
				score = records.get(itemid);
				outtest.println(itemid);
				System.out.println("testFile: " + itemid);
				outreal.println(itemid + "    " + score);
				System.out.println("realFile: " + itemid + "    " + score);
			}
			records.clear();
		}
			
		
		if(scanner != null){
			scanner.close();
		}
		if(outtest != null){
			outtest.close();
		}
		if(outreal != null){
			outreal.close();
		}
		
	}

	
}