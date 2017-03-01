package com.tian.preceptron;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Preceptron {
	/**
	 * @author tianliuliu
	 * @date : 2017-2-28 下午7:19:02
	 */
	// 训练集x
	static int[][] x = new int[100][100];
	// 训练集y
	static int[] y = new int[100];
	// 参数w
	static float[] w = new float[100];
	// 偏置值
	static float b = 0;
	// 初始的偏置量
	static float b0 = 0;
	// 初始的w
	static float w0 = 0;
	// 得到数据集的行数
	static int m = 0;
	// 得到数据集的列数
	static int n = 0;
	// 参数值
	static float yita = 1;

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
				int item = Integer.valueOf(items[j]);
				x[i][j] = item;
			}
			y[i] = Integer.valueOf(items[len]);
			line = br.readLine();
			i += 1;
		}
		m = i;
		n = len;
	}

	// 参数初始化
	public static void init() {
		for (int i = 0; i < n; i++) {
			w[i] = w0 + yita * y[0] * x[0][i];
		}
		b = b0 + yita * y[0];
	}

	// 训练感知机模型
	public static void trainPreceptron() {

		for (int i = 0; i < m; i++) {
			int sum = 0;
			for (int j = 0; j < n; j++) {
				sum += w[j] * x[i][j];
			}
			sum += b;
			if (y[i] * sum <= 0) {
				for (int k = 0; k < n; k++) {
					w[k] = w[k] + yita * y[i] * x[i][k];
				}
				b = b + yita * y[i];
				trainPreceptron();
			}
		}

	}

	// 打印结果
	public static void printResult() {
		for (int k = 0; k < n; k++) {
			System.out.print(w[k] + " ");
		}
		System.out.println(b);

	}

	public static void main(String[] args) {
		String trainfile = "d:\\testPreceptron.txt";
		try {
			readFile(trainfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		init();
		trainPreceptron();
		printResult();
	}

}
