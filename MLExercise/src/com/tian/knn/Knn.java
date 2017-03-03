package com.tian.knn;

import java.util.Collections;
import java.util.Vector;

/**
 * @author tianliuliu
 * @date : 2017-3-2 下午07:43:50
 */ 
public class Knn {
	
	
	static int data[][] = {{2,3},{5,4},{9,6},{4,7},{8,1},{7,2}};
	
	//构建kd树  
	public static void buildKdTree(KdTree tree,Vector<Vector<Double>> alldata,int depth){
		int sampleNum = alldata.size();
		if(sampleNum==0){
			return;
		}
		if(sampleNum==1){
			tree.root=alldata.get(0);
			return;
		}
		//维度
		int k = alldata.get(0).size();
		Vector<Vector<Double>> transData = Transpose(alldata);
		 //选择切分属性  
	    int splitAttribute = depth % k; 
	    
	    Vector<Double> splitAttributeValues = transData.get(splitAttribute);
	    //选择切分值  
	    double splitValue = findMiddleValue(splitAttributeValues);
	    
	    
	 // 根据选定的切分属性和切分值，将数据集分为两个子集
        Vector<Vector<Double> > subset1 = new Vector<Vector<Double>>();
	    Vector<Vector<Double> > subset2 =new Vector<Vector<Double>>();
	   for(int i=0;i<sampleNum;i++){
		   if (splitAttributeValues.get(i) == splitValue && tree.root==null){
			   tree.root = alldata.get(i);
		   }
		   else{
			   if (splitAttributeValues.get(i) < splitValue){
				   subset1.add(alldata.get(i));
			   }else{
				   subset2.add(alldata.get(i));
			   }
		   }
		   
	   }
	   tree.leftChild = new KdTree();
	   tree.leftChild.parent = tree;
	   tree.rightChild = new KdTree();
	   tree.rightChild.parent = tree;
	   buildKdTree(tree.leftChild, subset1, depth+1);
	   buildKdTree(tree.rightChild, subset2, depth+1);
	}
	//打印kd树
	private static void printKdTree(KdTree kdTree, int depth) {
		// TODO Auto-generated method stub
		
		for(int i=0;i<depth;i++){
			System.out.print("\t");
		}
		
		for(int j=0;j<kdTree.root.size();j++){
			System.out.print(kdTree.root.get(j)+",");
		}
		System.out.println();
		if(kdTree.leftChild==null&&kdTree.rightChild==null){
			return;
		}
		else{
			if(kdTree.leftChild!=null&&kdTree.leftChild.root!=null){
				for(int i=0;i<depth+1;i++)
					System.out.print("\t");
				System.out.print("left:");
				printKdTree(kdTree.leftChild, depth+1);
			}
			System.out.println();
			if(kdTree.rightChild!=null&&kdTree.rightChild.root!=null){
				for(int i=0;i<depth+1;i++)
					System.out.print("\t");
				System.out.print("right:");
				printKdTree(kdTree.rightChild, depth+1);
			}
			System.out.println();
		}
		
		
	}
	//查找中位数
	private static double findMiddleValue(Vector<Double> splitAttributeValues) {
		// TODO Auto-generated method stu
		Collections.sort(splitAttributeValues);
		int len = splitAttributeValues.size();
		
		return splitAttributeValues.get(len/2);
	}
   //转置向量矩阵
	private static Vector<Vector<Double>> Transpose(Vector<Vector<Double>> data2) {
		// TODO Auto-generated method stub
		int row = data2.size();  
	    int col = data2.get(0).size();  
	    Vector<Vector<Double>> tData = new Vector<Vector<Double>>();  
	    for (int i = 0; i < col; i++)  
	    {  
	    	Vector<Double> linedata = new Vector<Double>();
	        for (int j = 0; j < row; j++)  
	        {  
	            linedata.add(data2.get(j).get(i)) ;  
	        } 
	        tData.add(linedata);
	    }  
	    return tData; 
	}
	//在kd树tree中搜索目标点goal的最近邻
	//输入：目标点；已构造的kd树
	//输出：目标点的最近邻
	private static Vector<Double> searchNearestNeighbor(Vector<Double> goal,
			KdTree kdTree) {
		// TODO Auto-generated method stub
		 /*第一步：在kd树中找出包含目标点的叶子结点：从根结点出发，
		   递归的向下访问kd树，若目标点的当前维的坐标小于切分点的
		   坐标，则移动到左子结点，否则移动到右子结点，直到子结点为
		  叶结点为止,以此叶子结点为“当前最近点”
	     */
		int k = kdTree.root.size();//当前数据的维数
		
		int d=0;//从第0维开始
		
		KdTree currentTree = kdTree;
		Vector<Double> currentNearest = currentTree.root;
		while(!currentTree.isLeaf()){
			int index = d%k;
			if (currentTree.rightChild == null || goal.get(index) < currentNearest.get(index)){
				currentTree = currentTree.leftChild;
			}
			else{
				currentTree = currentTree.rightChild;
			}
			d+=1;
		}
		currentNearest = currentTree.root;
		
		/*第二步：递归地向上回退， 在每个结点进行如下操作：
		     (a)如果该结点保存的实例比当前最近点距离目标点更近，则以该例点为“当前最近点”
		     (b)当前最近点一定存在于某结点一个子结点对应的区域，检查该子结点的父结点的另
		     一子结点对应区域是否有更近的点（即检查另一子结点对应的区域是否与以目标点为球
		    心、以目标点与“当前最近点”间的距离为半径的球体相交）；如果相交，可能在另一
		     个子结点对应的区域内存在距目标点更近的点，移动到另一个子结点，接着递归进行最
		     近邻搜索；如果不相交，向上回退*/
		
		double currentDistance = measureDistance(goal, currentNearest, 0);
		
		
		//如果当前子kd树的根结点是其父结点的左孩子，则搜索其父结点的右孩子结点所代表
	    //的区域，反之亦反
	    KdTree searchDistrict;
	    if (currentTree.isLeft())
	    {
	        if (currentTree.parent.rightChild == null)
	            searchDistrict = currentTree;
	        else
	            searchDistrict = currentTree.parent.rightChild;
	    }
	    else
	    {
	        searchDistrict = currentTree.parent.leftChild;
	    }

	    //如果搜索区域对应的子kd树的根结点不是整个kd树的根结点，继续回退搜索
	    while (searchDistrict.parent != null)
	    {
	        //搜索区域与目标点的最近距离
	        double districtDistance = Math.abs(goal.get((d+1)%k) - searchDistrict.parent.root.get((d+1)%k));

	        //如果“搜索区域与目标点的最近距离”比“当前最近邻与目标点的距离”短，表明搜索
	        //区域内可能存在距离目标点更近的点
	        if (districtDistance < currentDistance )//&& !searchDistrict->isEmpty()
	        {

	            double parentDistance = measureDistance(goal, searchDistrict.parent.root, 0);

	            if (parentDistance < currentDistance)
	            {
	                currentDistance = parentDistance;
	                currentTree = searchDistrict.parent;
	                currentNearest = currentTree.root;
	            }
	            if (!searchDistrict.isEmpty())
	            {
	                double rootDistance = measureDistance(goal, searchDistrict.root, 0);
	                if (rootDistance < currentDistance)
	                {
	                    currentDistance = rootDistance;
	                    currentTree = searchDistrict;
	                    currentNearest = currentTree.root;
	                }
	            }
	            if (searchDistrict.leftChild != null&&searchDistrict.leftChild.root !=null)
	            {
	                double leftDistance = measureDistance(goal, searchDistrict.leftChild.root, 0);
	                if (leftDistance < currentDistance)
	                {
	                    currentDistance = leftDistance;
	                    currentTree = searchDistrict;
	                    currentNearest = currentTree.root;
	                }
	            }
	            if (searchDistrict.rightChild !=null&&searchDistrict.rightChild.root !=null)
	            {
	                double rightDistance = measureDistance(goal, searchDistrict.rightChild.root, 0);
	                if (rightDistance < currentDistance)
	                {
	                    currentDistance = rightDistance;
	                    currentTree = searchDistrict;
	                    currentNearest = currentTree.root;
	                }
	            }
	        }//end if

	        if (searchDistrict.parent.parent != null)
	        {
	            searchDistrict = searchDistrict.parent.isLeft()? 
	                            searchDistrict.parent.parent.rightChild:
	                            searchDistrict.parent.parent.leftChild;
	        }
	        else
	        {
	            searchDistrict = searchDistrict.parent;
	        }
	        ++d;
	    }//end while
	    return currentNearest;
		
	}
  
	
	private static double measureDistance(Vector<Double> goal,
			Vector<Double> currentNearest, int method) {
		// TODO Auto-generated method stub
		 if (goal.size() != currentNearest.size())
		    {
		        System.err.println("Dimensions don't match！！" );
		       
		    }
		    switch (method)
		    {
		        case 0://欧氏距离
		            {
		                double res = 0;
		                for (int i = 0; i < goal.size(); ++i)
		                {
		                    res += Math.pow((goal.get(i) - currentNearest.get(i)), 2);
		                }
		                return Math.sqrt(res);
		            }
		        case 1://曼哈顿距离
		            {
		                double res = 0;
		                for (int i = 0; i < goal.size(); ++i)
		                {
		                    res += Math.abs(goal.get(i) - currentNearest.get(i));
		                }
		                return res;
		            }
		        default:
		            {
		                System.err.println("Invalid method!!");
		                return -1;
		            }
		    }
		
	}
	public static void main(String[] args) {
		
		Vector<Vector<Double>> traindata = new Vector<Vector<Double>>();
		for(int i=0;i<data.length;i++){
			Vector<Double> linedata = new Vector<Double>();
			for(int j=0;j<data[0].length;j++){
				linedata.add(Double.valueOf(data[i][j]));
			}
			traindata.add(linedata);
		}
		KdTree kdTree = new KdTree();
		buildKdTree(kdTree,traindata,0);
		
		printKdTree(kdTree, 0);
		
		Vector<Double> goal = new Vector<Double>();
		goal.add(3.0);
		goal.add(4.5);
		
		Vector<Double> nearestNeighbor = searchNearestNeighbor(goal, kdTree);
		System.out.print("The nearest neighbor is: ");
		for(Double near:nearestNeighbor){
			System.out.print(near+",");
		}
		System.out.println();
		
		
	}
	
	 

}
