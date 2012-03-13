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
    public static final String BAYES = "bayes";
    public static final String LOGISTIC = "logistic";

    public static final String PERCEPTRON = "perceptron";

    public static final String SVM_POLY = "svm_poly";
    public static final String SVM_RBF = "svm_rbf";

    public static final String KNN_1 = "knn_1";
    public static final String KNN_10 = "knn_10";

    public static final String TREE = "tree";
    public static final String BOOST_TREE = "boost_tree";
    public static final String BOOST_STUMP = "boost_stump";

    public static final String RANDOM_FOREST = "random_forest";

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

    public static Classifier getClassifier(String type) {
        Classifier classifier = null;
        if (type.equals(BAYES)) classifier = new NaiveBayes();
        if (type.equals(LOGISTIC)) classifier = new Logistic();
        if (type.equals(TREE)) classifier = new J48();
        if (type.equals(KNN_1)) classifier = new IBk();
        if (type.equals(KNN_10)) {
            IBk ibk = new IBk();
            SelectedTag t = ibk.getDistanceWeighting();
            weka.core.Tag[] tags = t.getTags();
            ibk.setDistanceWeighting(new SelectedTag(IBk.WEIGHT_INVERSE, tags));
            ibk.setKNN(10);
            classifier = ibk;
        }
        if (type.equals(PERCEPTRON)) classifier = new VotedPerceptron();
        if (type.equals(RANDOM_FOREST)) classifier = new RandomForest();
        if (type.equals(SVM_POLY)) {
            SMO s = new SMO();
            Kernel k = new PolyKernel();
            s.setKernel(k);
            classifier = s;
        }
        if (type.equals(SVM_RBF)) {
            SMO s = new SMO();
            RBFKernel r = new RBFKernel();
            r.setCacheSize(0);
            Kernel k = r;
            s.setKernel(k);

            classifier = s;
        }
        if (type.equals(BOOST_TREE)) {
            AdaBoostM1 a = new AdaBoostM1();
            a.setClassifier(new J48());
            classifier = a;
        }
        if (type.equals(BOOST_STUMP)) {
            AdaBoostM1 a = new AdaBoostM1();
            a.setClassifier(new DecisionStump());
            classifier = a;
        }

        return classifier;
    }

    public static Classifier trainClassifier(Instances in, String type, int clsAtt, String opts, String intOpts) {
        Classifier classifier = getClassifier(type);
        int classAttr = clsAtt - 1;
        try {
            if (!intOpts.equals("")) {
                if (type.equals(BOOST_TREE))
                    ((AdaBoostM1)classifier).getClassifier().setOptions(Utils.splitOptions(intOpts));
                else if (type.equals(SVM_RBF) || type.equals(SVM_POLY))
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

    public static Classifier trainClassifier(Instances in, String type, int classAttr, String opts) {
        return trainClassifier(in, type, classAttr, opts, "");
    }

    public static Classifier trainClassifier(Instances in, String type, int classAttr) {
        return trainClassifier(in, type, classAttr, "", "");
    }

    public static void listOptions(String type) {
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
        String s = BAYES + " - Naive Bayes Model\n" +
            LOGISTIC + " - Logistic Regression\n" +
            PERCEPTRON + " - Voted Perceptron\n" +
            SVM_POLY + " - SVM with Polynomial Kernel\n" +
            SVM_RBF + " - SVM with Radial Basis Function Kernel\n" +
            KNN_1 + " - 1-Nearest Neighbor\n" +
            KNN_10 + " - 10-Nearest Neighbor\n" +
            TREE + " - Decision Tree (J48)\n" +
            BOOST_TREE + " - Adaptive Boosting with Decision Trees\n" +
            BOOST_STUMP + " - Adaptive Boosting with Decision Stumps\n" +
            RANDOM_FOREST + " - Random Forests\n";
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
