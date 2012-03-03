package org.clparker.wekalab;

import weka.core.Instances;
import weka.core.Instance;
import weka.classifiers.Classifier;

public class WekaModel {
    public Classifier classifier = null;
    public Instances refSet = null;
    
    public WekaModel() {}
    
    public WekaModel(Instances in, int type, int classAttr, String opts, String intOpts) {
        trainModel(in, type, classAttr, opts, intOpts);
    }
    
    public WekaModel(Instances in, int type, int classAttr) {
        trainModel(in, type, classAttr, "", "");
    }

    public WekaModel(double[][] data, int type, int classAttr, String opts, String intOpts) {
        trainModel(data, type, classAttr, opts, intOpts);
    }

    public WekaModel(double[][] data, int type, int classAttr) {
        trainModel(data, type, classAttr, "", "");
    }
    
    public void trainModel(Instances in, int type, int classAttr, String opts, String intOpts) {
        refSet = in;
        classifier = WekaInterface.trainClassifier(refSet, type, classAttr, opts, intOpts);
    }
    
    public void trainModel(Instances in, int type, int classAttr) {
        refSet = in;
        classifier = WekaInterface.trainClassifier(refSet, type, classAttr, "", "");
    }

    public void trainModel(double[][] data, int type, int classAttr, String opts, String intOpts) {
        refSet = WekaInterface.createSet(data, classAttr);
        classifier = WekaInterface.trainClassifier(refSet, type, classAttr, opts, intOpts);
    }    

    public void trainModel(double[][] data, int type, int classAttr) {
        refSet = WekaInterface.createSet(data, classAttr);
        classifier = WekaInterface.trainClassifier(refSet, type, classAttr, "", "");
    }
    
    public int classify(Instance in) {
        return WekaInterface.classify(in, classifier);
    }
    
    public double[] getClassDistribution(Instance in) {
        return WekaInterface.getClassDistribution(in, classifier);
    }
    
    public int classify(double[] point) {
        Instance in = WekaInterface.createInstance(point, refSet.classIndex(), refSet);
        return WekaInterface.classify(in, classifier);
    }
    
    public double[] getClassDistribution(double[] point) {
        Instance in = WekaInterface.createInstance(point, refSet.classIndex(), refSet);
        return WekaInterface.getClassDistribution(in, classifier);
    }
    
    public int getNumClasses() {
        return refSet.classAttribute().numValues();
    }
    
    public int getNumAttributes() {
        return refSet.numAttributes();
    }
    
    public void setOptions(String s) {
        try {
            classifier.setOptions(s.split(","));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void printModel() {
        System.out.println(classifier.toString());
    }
}
