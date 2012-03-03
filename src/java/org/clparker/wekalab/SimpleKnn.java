package org.clparker.wekalab;

import java.util.ArrayList;

import org.clparker.utils.Libes;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class SimpleKnn extends Classifier {
    private static final long serialVersionUID = -352074370755473358L;

    public KnnSet set = new KnnSet();
    public int[] trainFiles;
    public int[] testFiles;

    public void buildClassifier(Instances arg0) throws Exception {
        set.ensureCapacity(arg0.numInstances());
        for (int i = 0; i < arg0.numInstances(); i++) {
            DataPoint d = new DataPoint(arg0.instance(i).toDoubleArray(), arg0.classIndex());
            set.add(d);
        }
    }

    public void buildClassifier(KnnSet k) {
        set = k;
    }
    
    public double[][] getData() {
        return set.getData();
    }
    
    public double[][] looCv() {
        double[][] preds = new double[set.size()][2];
        // int numPos = 0;
        ArrayList<DataPoint> temp = new ArrayList<DataPoint>();
        
        for (DataPoint d : set) temp.add(d);
        
        for (int i = 0; i < temp.size(); i++) {
            // System.out.println("********** " + temp.get(i).id.fileId + " **********");
            // System.out.println("********** " + temp.get(i).pointClass + " **********");
            preds[i] = set.getDist(temp.get(i));
            // if (temp.get(i).pointClass) numPos++;
        }
        // System.out.println(numPos);
        return preds;
    }
    
    public double classifyInstance(Instance in) {
        return Libes.maxInd(distributionForInstance(in));
    }
    
    public double[] distributionForInstance(Instance in) {
        DataPoint d = new DataPoint(in.toDoubleArray(), in.classIndex());
        return set.getDist(d);
    }
    
    public void setOptions(String[] s) {
        set.K = Integer.parseInt(s[0]);
    }
}
