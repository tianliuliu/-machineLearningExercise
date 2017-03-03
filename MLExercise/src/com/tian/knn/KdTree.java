package com.tian.knn;

import java.util.Vector;

public class KdTree {
	Vector<Double> root;
	
	KdTree parent;
	KdTree leftChild;
	KdTree rightChild;
	
	//判断kd树是否为空  
    public boolean isEmpty()  
    {  
        return root==null;  
    }  
	
    //判断kd树是否只是一个叶子结点  
    public boolean isLeaf()  
    {  
        return (!(root==null)) &&   
            rightChild == null && leftChild == null;  
    }  
    //判断是否是树的根结点  
    public boolean isRoot()  
    {  
        return  parent == null;  
    }
    //判断该子kd树的根结点是否是其父kd树的左结点  
    public boolean isLeft()  
    {  
        return parent.leftChild.root == root;  
    }
    //判断该子kd树的根结点是否是其父kd树的右结点  
    public boolean isRight()  
    {  
        return parent.rightChild.root == root;  
    }
    

}
