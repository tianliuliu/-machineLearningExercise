package com.tian.preceptron;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DuiOuPreceptron {
	
    /**
	 * @author tianliuliu
	 * @date : 2017-3-1 上午10:19:02
	 */ 
   
	//训练集x
	static int[][] x = new int[100][100];
	//训练集x的转置
	static int[][] xt = new int[100][100];
	//训练集y
	static int[] y = new int[100];
	//参数w
	static float[] alf = new float[100];
	//偏置值
	static int[][] G = new int[100][100];
	static float b = 0;
	//初始的偏置量
	static float b0 = 0;
	//得到数据集的行数
	static int m = 0;
	//得到数据集的列数
	static int n = 0;
	//参数值
	static float yita = 1;
	//参数w
	static float[] w = new float[100];
	
	
	//读取文件得到训练集
	public static  void readFile(String path) throws IOException{

        InputStreamReader inStrR = new InputStreamReader(new FileInputStream(path)); 
        BufferedReader br = new BufferedReader(inStrR); 
        String line = br.readLine();
        int i = 0;
        int len = 0; 
        while(line != null){
        	String[] items = line.split(" ");
        	len = items.length-1;
        	for(int j=0;j<len;j++){
        		int item = Integer.valueOf(items[j]);
        		x[i][j] = item;
        	}
        	y[i]=  Integer.valueOf(items[len]);
            line = br.readLine();  
            i+=1;
        }
        m = i;
        n = len;
	}
	//参数初始化
	public static void init(){
		
		for(int i=0;i<m;i++){
			alf[i] = 0;
		}
		alf[0]=alf[0]+yita;
		b=b0+yita*y[0];
	}
	//计算矩阵g
	public static void caclG(){
		//对矩阵x进行转置
		for(int i=0;i<m;i++){
			for(int j=0;j<n;j++){
				xt[j][i]=x[i][j];
			}
			
		}
		//x自身与转置矩阵相乘
		for(int i=0;i<m;i++){
			for(int j=0;j<m;j++){
			int sum = 0;
			for(int k=0;k<n;k++){
				sum += x[i][k]*xt[k][j];
			}
			G[i][j]=sum;
		   }
		}
	}
	//训练感知机模型
	public static void trainPreceptron(){
		
		for(int i=0;i<m;i++){
			int sum = 0;
			for(int j=0;j<m;j++){
			    sum+=alf[j]*y[j]*G[j][i];
			}
			sum+=b;
			if(y[i]*sum<=0){
			    alf[i] = alf[i]+yita;
				b=b+yita*y[i];
				trainPreceptron();
			}
		}
		
	}
	//得到参数w
	public static void getW(){
		
			for(int i=0;i<n;i++){
			int sum = 0;
			for(int j=0;j<m;j++){
				sum += y[j]*alf[j]*x[j][i];
			}
			w[i]=sum;
		   }
	}
	//打印alf的值和w的值以及b的值
	public static void printResult(){
		for(int k=0;k<m;k++){
			System.out.print(alf[k]+" ");
		}
		System.out.println();
		for(int k=0;k<n;k++){
			System.out.print(w[k]+" ");
		}
		System.out.println(b);
		
	}
	//程序入口
	public static void main(String[] args) {
		String trainfile = "d:\\testPreceptron.txt";
		try {
			readFile(trainfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		init();
		caclG();
		try{
			trainPreceptron();
		}catch (Exception e) {
			System.out.println("您的数据不适合感知机模型");
		}
		
		getW();
		printResult();
	}

}
