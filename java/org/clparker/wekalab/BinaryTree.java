package org.clparker.wekalab;

import org.clparker.utils.Libes;
import org.clparker.wekalab.DataPoint;
import org.clparker.wekalab.PointSet;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class BinaryTree extends Classifier {
    private static final long serialVersionUID = -8724235805268903620L;
    private static int NUM_TREES = 25;
    
    public int measure = -1;
    public int classIndex = -1;
    public TreeNode[] roots;
    
    public BinaryTree(int type) { 
        measure = type;
        roots = new TreeNode[NUM_TREES];
    }

    public BinaryTree(int type, int numTrees) { 
        measure = type;
        NUM_TREES = numTrees;
        roots = new TreeNode[NUM_TREES];
    }

    public double classifyInstance(Instance in) {
        return Libes.maxInd(distributionForInstance(in));
    }
    
    public double[] distributionForInstance(Instance in) {
        double[] dist = new double[2];
        DataPoint d = new DataPoint(in.toDoubleArray(), classIndex);
        for (TreeNode r : roots)
            dist[r.classify(d.data)]++;
        dist[0] /= roots.length;
        dist[1] /= roots.length;
        return dist;
    }
    
    public void buildClassifier(Instances in) throws Exception {
        double[][] data = WekaInterface.setToDouble(in);
        classIndex = in.classIndex();
        PointSet p = new PointSet(data, classIndex);
        trainTree(p);
    }

    public void trainTree(PointSet p) {
        for (int i = 0; i < roots.length; i++) {
            System.out.print(".");
            roots[i] = new TreeNode(p.resample()); 
        }
        System.out.println();
    }
    
    public class TreeNode {
        public TreeNode left;
        public TreeNode right;
        
        public boolean nodeClass;
        public int column;
        public double threshold;
        
        public TreeNode(PointSet p) {
            if (p.isUniform()) {
                left = right = null;
                column = -1;
                threshold = 0;
                nodeClass = p.get(0).pointClass;
            }
            else {
                PointSet[] sets = p.getBestSplit(measure);
                if (sets[0].size() == 0 || sets[1].size() == 0) {
                    System.out.println("Invoking getMajority; p: " + p.positives + " n: " + p.negatives + " col: " + p.bestColumn + " score: " + p.bestScore);
                    p.writeArffFile("badSplit.arff");
                    left = right = null;
                    column = -1;
                    threshold = 0;
                    nodeClass = p.getMajority();                
                }
                else {
                    column = p.bestColumn;
                    threshold = p.bestThreshold;
                    left = new TreeNode(sets[0]);
                    right = new TreeNode(sets[1]);
                }
                
            }
        }
        
        public int classify(double[] data) {
            if (left == null || right == null) {
                if (nodeClass) return 1;
                else return 0;
            }
            else {
                if (data[column] < threshold) return left.classify(data);
                else return right.classify(data);
            }
        }
     
    }
    
    public static void main(String[] args) {
        Instances in = WekaInterface.readArffFile("test-1.arff");
        in.setClassIndex(in.numAttributes() - 1);
        double[][] data = WekaInterface.setToDouble(in);
        int cInd = in.classIndex();
        PointSet p = new PointSet(data, cInd);
        BinaryTree b = new BinaryTree(PointSet.F1);
        b.trainTree(p);
    }

}
