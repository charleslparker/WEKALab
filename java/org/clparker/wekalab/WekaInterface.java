package org.clparker.wekalab;

import java.util.Enumeration;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.VotedPerceptron;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;

import weka.core.Instances;
import weka.core.Instance;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Option;
import weka.core.SelectedTag;
import weka.classifiers.Classifier;
import weka.core.Utils;

import org.clparker.utils.Writer;
import org.clparker.utils.Libes;

public class WekaInterface {
    public static final int BAYES = 0;
    public static final int LOGISTIC = 1;

    public static final int PERCEPTRON = 2;

    public static final int SVM_POLY = 3;
    public static final int SVM_RBF = 4;

    public static final int KNN_1 = 5;
    public static final int KNN_10 = 6;
    
    public static final int TREE = 7;
    public static final int ADABOOST_TREE = 8;
    public static final int ADABOOST_STUMP = 9;
    
    public static final int RANDOM_FOREST = 10;
    
    public static final int F1_TREE = 11;
    public static final int F1_BOOST = 12;
    public static final int INFO_GAIN_TREE = 13;

    public static final int SIMPLE_KNN = 14;
    public static final int KNN3 = 15;

    public static Instances readArffFile(String file) {
        try {
            Instances in = new Instances(new java.io.FileReader(file));
            return in;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Classifier getClassifier(int type) {
        Classifier classifier = null;
        if (type == BAYES) classifier = new NaiveBayes();
        if (type == LOGISTIC) classifier = new Logistic();
        if (type == TREE) classifier = new J48();
        if (type == KNN_1) classifier = new IBk();
        if (type == KNN_10) {
            IBk ibk = new IBk();
            SelectedTag t = ibk.getDistanceWeighting();
            weka.core.Tag[] tags = t.getTags();
            ibk.setDistanceWeighting(new SelectedTag(IBk.WEIGHT_INVERSE, tags));
            ibk.setKNN(10);
            classifier = ibk;
        }
        if (type == PERCEPTRON) classifier = new VotedPerceptron();
        if (type == RANDOM_FOREST) classifier = new RandomForest();
        if (type == SVM_POLY) {
            SMO s = new SMO();
            Kernel k = new PolyKernel();
            s.setKernel(k);
            classifier = s;
        }
        if (type == SVM_RBF) {
            SMO s = new SMO();
            RBFKernel r = new RBFKernel();
            r.setCacheSize(0);
            Kernel k = r;
            s.setKernel(k);
            
            classifier = s;
        }
        if (type == ADABOOST_TREE) {
            AdaBoostM1 a = new AdaBoostM1();
            a.setClassifier(new J48());
            classifier = a;
        }
        if (type == ADABOOST_STUMP) {
            AdaBoostM1 a = new AdaBoostM1();
            a.setClassifier(new DecisionStump());
            classifier = a;
        }

        if (type == F1_TREE) classifier = new J48F1();
        if (type == F1_BOOST) classifier = new BinaryTree(PointSet.F1);
        if (type == INFO_GAIN_TREE) classifier = new BinaryTree(PointSet.INFO_GAIN);
        
        if (type == SIMPLE_KNN) {
            classifier = new SimpleKnn();
        }
        
        if (type == KNN3) {
            IBk ibk = new IBk();
            SelectedTag t = ibk.getDistanceWeighting();
            weka.core.Tag[] tags = t.getTags();
            ibk.setDistanceWeighting(new SelectedTag(IBk.WEIGHT_INVERSE, tags));
            ibk.setKNN(3);
            classifier = ibk;
        }

        return classifier;
    }
    
    public static Classifier trainClassifier(Instances in, int type, int clsAtt, String opts, String intOpts) {
        Classifier classifier = getClassifier(type);
        int classAttr = clsAtt - 1;
        try {
            if (!intOpts.equals("")) {
                if (type == ADABOOST_TREE)
                    ((AdaBoostM1)classifier).getClassifier().setOptions(Utils.splitOptions(intOpts));
                else if (type == SVM_RBF || type == SVM_POLY)
                    ((SMO)classifier).getKernel().setOptions(Utils.splitOptions(intOpts));
                else
                    System.err.println("Warning: Classifier type does not support internal options!");
            }
            if (!opts.equals("")) {
                classifier.setOptions(Utils.splitOptions(opts));
            }

            in.setClassIndex(classAttr);
            classifier.buildClassifier(in);
            return classifier;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Classifier trainClassifier(Instances in, int type, int classAttr, String opts) {
        return trainClassifier(in, type, classAttr, opts, "");
    }
    
    public static Classifier trainClassifier(Instances in, int type, int classAttr) {
        return trainClassifier(in, type, classAttr, "", "");
    }

    public static void listOptions(int type) {
        Classifier classifier = getClassifier(type);
        Enumeration<?> e = classifier.listOptions();

        while (e.hasMoreElements()) {
            Option o = ((Option)e.nextElement());
            System.out.println(o.synopsis() + " - " + o.numArguments() + " argument(s)");
            System.out.println();
            System.out.println(o.description());
            System.out.println();
        }
    }
    
    public static double[] getClassDistribution(Instance in, Classifier c) {
        try {
            return c.distributionForInstance(in);
        }
        catch (Exception e) {
            System.out.println("Exception in getClassDistribution!");
            e.printStackTrace();
        }
        return null;
    }
    
    public static int classify(Instance in, Classifier c) {
        try {
            return (int)c.classifyInstance(in);
        }
        catch (Exception e) {
            System.out.println("Exception in getClassDistribution!");
            e.printStackTrace();
        }
        return -1;
    }

    public static Instances createSet(double[][] d, int clsAtt) {
        if (d.length == 0) return null;
        int classAttr = clsAtt - 1;
        int numClasses = (int)Libes.maxInColumn(d, classAttr) + 1;
        FastVector attInfo = new FastVector(d[0].length);
        FastVector classes = new FastVector(numClasses);

        for (int i = 0; i < numClasses; i++)
            classes.addElement(i + "");
        
        for (int i = 0; i < d[0].length; i++) {
            if (i != classAttr) attInfo.addElement(new Attribute("att" + i));
            else attInfo.addElement(new Attribute("class", classes));
        }
        
        Instances in = new Instances("weka-relation", attInfo, d.length);
        
        for (int i = 0; i < d.length; i++) {
            in.add(createInstance(d[i], classAttr, in));
        }
        
        in.setClassIndex(classAttr);
        
        return in;
    }
    
    public static void writeSet(Instances in, String file) {
        Writer w = new Writer(file);
        w.fileOnly();
        w.writeLine("%@SIZE " + in.numAttributes() + " " + in.numInstances());
        w.writeLine(in.toString());
        w.close();
    }
    
    public static void writeArffFile(double[][] data, int clsInd, String file) {
        Instances in = createSet(data, clsInd);
        writeSet(in, file);
    }
    
    public static void listClassifiers() {
        String s = "WekaInterface.BAYES - Naive Bayes Model\n" +
            "WekaInterface.LOGISTIC - Logistic Regression\n" +
            "WekaInterface.PERCEPTRON - Voted Perceptron\n" +
            "WekaInterface.SVM_POLY - SVM with Polynomial Kernel\n" +
            "WekaInterface.SVM_RBF - SVM with Radial Basis Function Kernel\n" +
            "WekaInterface.KNN_1 - 1-Nearest Neighbor\n" +
            "WekaInterface.KNN_10 - 10-Nearest Neighbor\n" +
            "WekaInterface.TREE - Decision Tree (J48)\n" +
            "WekaInterface.ADABOOST_TREE - Adaptive Boosting with Decision Trees\n" +
            "WekaInterface.ADABOOST_STUMP - Adaptive Boosting with Decision Stumps\n" +
            "WekaInterface.RANDOM_FOREST - Random Forests\n";
        System.out.println(s);
    }
    
    public static Instance createInstance(double[] point, int classAttr, Instances in) {
        Instance newIn = new Instance(in.numAttributes());
        for (int j = 0; j < in.numAttributes(); j++) {
            Attribute att = in.attribute(j);
            if (j != classAttr)
                newIn.setValue(att, point[j]);
            else 
                newIn.setValue(att, "" + (int)(point[j]));
        }
        newIn.setDataset(in);
        return newIn;
    }

    public static Instance createInstance(String[] point, Instances in) {
        Instance newIn = new Instance(in.numAttributes());
        for (int j = 0; j < in.numAttributes(); j++) {
            Attribute att = in.attribute(j);
            if (!point[j].isEmpty() && !point[j].equals("?")) {
                if (att.isNominal())
                    newIn.setValue(att, point[j]);
                else 
                    newIn.setValue(att, Double.parseDouble(point[j]));
            }
            else {
                newIn.setMissing(att);
            }
        }
        newIn.setDataset(in);
        return newIn;
    }
    
    public static double[][] setToDouble(Instances in) {
        double[][] set = new double[in.numInstances()][in.numAttributes()];
        for (int i = 0; i < in.numInstances(); i++) {
            set[i] = in.instance(i).toDoubleArray();
        }
        return set;
    }

    public static int[][] testClassifier(Classifier c, Instances t) {
        int[][] cm = new int[t.classAttribute().numValues()][t.classAttribute().numValues()];
        for (int i = 0; i < t.numInstances(); i++) {
            int cl = WekaInterface.classify(t.instance(i), c);
            cm[(int)Math.round(t.instance(i).classValue())][cl]++;
        }
        return cm;
    }
}