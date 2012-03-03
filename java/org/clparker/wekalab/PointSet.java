package org.clparker.wekalab;

import java.util.ArrayList;
import java.util.Collections;

import org.clparker.utils.Libes;

public class PointSet extends ArrayList<DataPoint> {
    private static final long serialVersionUID = -5461510472311653674L;

    public static final double EPS = 1E-8;

    public static final int F1 = 0;
    public static final int PRE = 1;
    public static final int REC = 2;
    public static final int INFO_GAIN = 3; //not finished!
    
    public double positives;
    public double negatives;
    public double hy;
    
    public double[] thresholds;
    public double[] scores;
    
    public int bestColumn = -1;
    public double bestThreshold = 0;
    public double bestScore = 0;
    
    public PointSet(double[][] data, int clsInd) {
        positives = 0;
        negatives = 0;
        for (int i = 0; i < data.length; i++)
            add(new DataPoint(data[i], clsInd));
        for (DataPoint d : this) {
            if (d.pointClass) positives++;
            else negatives++;
        }
        hy = getEntropy();
    }
    
    public PointSet() {
        positives = negatives = 0;
    }
    
    public PointSet resample() {
        PointSet p = new PointSet();
        for (int i = 0; i < size(); i++) {
            int rInd = (int)(Libes.random()*size());
            p.add(get(rInd));
            if (get(rInd).pointClass) p.positives++;
            else p.negatives++;
        }
        return p;
    }
    
    public void sort(int key) {
        DataPoint.sortKey = key;
        Collections.sort(this);
        // Just make it so that nothing else can sort effectively
        DataPoint.sortKey = -1;
    }
    
    public double get(int i, int j) {
        return get(i).data[j];
    }
    
    public PointSet[] getBestSplit(int measure) {
        int featCount = get(0).data.length;
        bestScore = Double.NEGATIVE_INFINITY;
        
        scores = new double[featCount];
        thresholds = new double[featCount];
        
        for (int i = 0; i < featCount; i++)
            getMeasure(measure, i);

        for (int i = 0; i < featCount; i++) {
            if (scores[i] > bestScore) {
                bestScore = scores[i];
                bestThreshold = thresholds[i];
                bestColumn = i;
            }
        }
        
        return split(bestColumn, bestThreshold);
    }
    
    public boolean isUniform() {
        DataPoint first = get(0);
        for (DataPoint d : this)
            if (d.pointClass != first.pointClass) return false; 
        return true;
    }
    
    public boolean getMajority() {
        double numPos = 0;
        for (DataPoint d : this)
            if (d.pointClass) numPos++;
        if (numPos > (double)size()/2) return true;
        return false;
    }
    
    public PointSet[] split(int column, double thresh) {
        PointSet[] sets = new PointSet[2];
        sets[0] = new PointSet();
        sets[1] = new PointSet();
             
        for (DataPoint d : this) {
            if (d.data[column] < thresh) {
                sets[0].add(d);
                if (d.pointClass) sets[0].positives++;
                else sets[0].negatives++;
            }
            else {
                sets[1].add(d);
                if (d.pointClass) sets[1].positives++;
                else sets[1].negatives++;
            }
        }
        sets[0].hy = sets[0].getEntropy();
        sets[1].hy = sets[1].getEntropy();
        

        /*
        double bits = this.getEntropy();
        double s1Bits = sets[0].getEntropy();
        double s2Bits = sets[1].getEntropy();
        
        double s1prob = (double)sets[0].size()/this.size();
        double s2prob = (double)sets[1].size()/this.size();
        
        double gain = bits - (s1prob*s1Bits + s2prob*s2Bits);

        
        System.out.println("Column: " + column);
        System.out.println("Threshold: " + thresh);
        System.out.println("Score: " + bestScore);
        System.out.println("Bits: " + bits);
        System.out.println("Gain: " + gain);
        /*
        System.out.println("Size: " + size());
        System.out.println("Positives: " + positives);
        System.out.println("Negatives: " + negatives);
        System.out.println("S1 Size: " + sets[0].size());
        System.out.println("S1 Positives: " + sets[0].positives);
        System.out.println("S1 Negatives: " + sets[0].negatives);
        System.out.println("S2 Size: " + sets[1].size());
        System.out.println("S2 Positives: " + sets[1].positives);
        System.out.println("S2 Negatives: " + sets[1].negatives);
        
        if (sets[0].size() == 0) {
            System.out.println("Empty Set 0!");
            System.out.println("Column " + column);
            System.out.println("Threshold " + thresh);
            System.out.println("Size " + size());
            System.out.println("Pos " + positives);
            System.out.println("Neg " + negatives);
        }
        */
        return sets;
    }
    
    public void getMeasure(int measure, int key) {
        double tpl = 0;
        double fpl = 0;
        double tnl = negatives;
        double fnl = positives;

        double tpg = positives;
        double fpg = negatives;
        double tng = 0;
        double fng = 0;

        double bestThresh = 0;
        double bestMeasure = 0;
        
        sort(key);
        // System.out.println("New Sort!");
        for (int i = 0; i < size() - 1; i++) {
            DataPoint d = get(i);
            // if (i < 10) System.out.println(d.data[key]);
            if (d.pointClass) {
                tpl++;
                fnl--;
                tpg--;
                fng++;
            }
            else {
                fpl++;
                tnl--;
                fpg--;
                tng++;
            }
            
            
            if (!approxEquals(d.data[key], get(i + 1).data[key])) {
                double perfl = computeMeasure(tpl, fpl, tnl, fnl, measure);
                double perfg = computeMeasure(tpg, fpg, tng, fng, measure);
                
                if (perfl > bestMeasure) {
                    bestThresh = (get(i).data[key] + get(i + 1).data[key])/2;
                    bestMeasure = perfl;
                }
                if (perfg > bestMeasure) {
                    bestThresh = (get(i).data[key] + get(i + 1).data[key])/2;
                    bestMeasure = perfg;
                }
            }
        }
        thresholds[key] = bestThresh;
        scores[key] = bestMeasure;
        /*
        System.out.println("Thresholds " + key +  ": " + thresholds[key]);
        System.out.println("Scores " + key +  ": " + scores[key]);
        */
    }
    
    public double computeMeasure(double tp, double fp, double tn, double fn, int measure) {
        double pre = tp/(tp + fp);
        double rec = tp/(tp + fn);
        double f1 = (pre*rec)/(pre + rec);
        
        switch (measure) {
        case F1:
            return f1;
        case PRE:
            return pre;
        case REC:
            return rec;
        case INFO_GAIN:
            double s1count = tp + fp;
            double s2count = tn + fn;
            double s1prob = s1count/size();
            double s2prob = s2count/size();
            double hygx = s1prob*setEntropy(tp, fp, s1count) + s2prob*setEntropy(tn, fn, s2count);

            /*
            if (hygx < 0.94) {
                System.out.println("TP: " + tp);
                System.out.println("FP: " + fp);
                System.out.println("TN: " + tn);
                System.out.println("FN: " + fn);
                System.out.println("setEntropy(tp, fp, s1count): " + setEntropy(tp, fp, s1count));
                System.out.println("setEntropy(tn, fn, s2count): " + setEntropy(tn, fn, s2count));
                System.out.println("hygx: " + hygx);
            }
            */
            return hy - hygx;
        default:
            return -1;
        }
    }
    
    public static double setEntropy(double pos, double neg, double size) {
        double posprob = pos/size;
        double negprob = neg/size;
        return entropy(posprob, negprob);
    }
    
    public double getEntropy() {
        return setEntropy(positives, negatives, size());
    }
    
    public static double entropy(double p1, double p2) {
        double s1 = 0;
        double s2 = 0;
        if (p1 != 0) s1 = p1*Libes.log2(p1);
        if (p2 != 0) s2 = p2*Libes.log2(p2);
        return -(s1 + s2);
    }
    
    public void writeArffFile(String file) {
        if (isEmpty()) return;
        double[][] data = new double[size()][get(0).data.length];
        for (int i = 0; i < size(); i++)
            data[i] = get(i).toDoubleArray();
        WekaInterface.writeArffFile(data, data[0].length, file);
    }
    
    public static void main(String args[]) {
        System.out.println(setEntropy(1, 2, 3));
        System.out.println(0.99*Libes.log2(0.99));
        System.out.println(0.01*Libes.log2(0.01));
    }
    
    public static boolean approxEquals(double x, double y) {
        double diff = Math.abs(x - y);
        if (diff / x < EPS) return true;
        if (x == 0 && y == 0) return true;
        return false;
    }
}
