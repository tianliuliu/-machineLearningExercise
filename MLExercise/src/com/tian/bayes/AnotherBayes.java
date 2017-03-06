package com.tian.bayes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class AnotherBayes {
	/**
	 * @author tianliuliu
	 * @date : 2017-3-5 下午08:27:47
	 */ 
	// 训练集x
	static String[][] x = new String[100][100];
	// 训练集y
	static String[] y = new String[100];
	
	// 得到数据集的行数
	static int m = 0;
	// 得到数据集的列数
	static int n = 0;
	
	//参数lmdma
	static double lmdal = 1.0;
	
	//得到x每个维数据的集合
	static List<Set<String>> allsets = new ArrayList<Set<String>>();
	//得到y中每个值的数目
	static Map<String,Integer> yValue = new HashMap<String,Integer>();
	//得到y中每个值的先验概率
	static Map<String,Double> yValuePro = new HashMap<String,Double>();
	//得到x对应y的先验概率
	static Map<String,Double> valuePro = new HashMap<String,Double>();
	
	// 读取文件得到训练集
	public static void readFile(String path) throws IOException {

		InputStreamReader inStrR = new InputStreamReader(new FileInputStream(
				path));
		BufferedReader br = new BufferedReader(inStrR);
		String line = br.readLine();
		int i = 0;
		int len = 0;
		while (line != null) {
			String[] items = line.split(" ");
			len = items.length - 1;
			for (int j = 0; j < len; j++) {
	
				x[i][j] = items[j];
			}
			y[i] = items[len];
			line = br.readLine();
			i += 1;
		}
		m = i;
		n = len;
	}
	
	//得到每一个维的集合
	public static void getSets(){
		
		
		for(int i=0;i<n;i++){
			Set<String> cloumn = new HashSet<String>();
			for(int j=0;j<m;j++){
				cloumn.add(x[j][i]);
			}
			allsets.add(cloumn);	
		}
		
	}
	//计算y中每个值的先验概率
	public static void getYXianyan(){
		
		
		for(int i=0;i<m;i++){
			if(yValue.get(y[i])==null){
				yValue.put(y[i], 1);
			}else{
				yValue.put(y[i],yValue.get(y[i])+1);
			}
		}
		int len = yValue.size();
		for(Map.Entry<String, Integer> entry:yValue.entrySet()){
			double yvalue = entry.getValue();
			
			yValuePro.put(entry.getKey(), (yvalue+lmdal)/(m+len*lmdal));
		}
		
		
	}
	//得到xy的贝叶斯模型
	public static void trainBayes(){
		
		for(int k=0;k<n;k++){
			for(String xitem:allsets.get(k)){
				for(Map.Entry<String, Integer> entry:yValue.entrySet()){
					double count = 0;
					for(int i=0;i<n;i++){
						for(int j=0;j<m;j++){
							
							if(x[j][i].equals(xitem)&&y[j].equals(entry.getKey())){
								count+=1.00;
							}
						}
					}
					valuePro.put(xitem+entry.getKey(),(count+lmdal)/(entry.getValue()+allsets.get(k).size()*lmdal));			
				}
			}
		}
		
		
	}
	//test 得到对应每个数据集的概率值
	public static void test(){
		String[] tests = new String[]{"2","S"};
		Map<String,Double> results = new HashMap<String,Double>();
		for(Map.Entry<String,Double> entry:yValuePro.entrySet()){
			double theResult = 1.0;
			for(int i=0;i<tests.length;i++){
				theResult*=valuePro.get(tests[i]+entry.getKey());
			}
			theResult*=entry.getValue();
			results.put(entry.getKey(), theResult);
		}
		System.out.println(results);
		
		
	}
	public static void main(String[] args) {
		
		String trainfile = "d:\\testBayes.txt";
		try {
			readFile(trainfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getSets();
		getYXianyan();
		trainBayes();
		test();
		
	}
	
	

}
