package org.clparker.wekalab;

import weka.classifiers.trees.j48.Distribution;
import weka.classifiers.trees.j48.SplitCriterion;

public class F1SplitCrit extends SplitCriterion {
    private static final long serialVersionUID = 7093417740400035758L;
    private static final double EPS = 0.000001;
    
    public final double splitCritValue(Distribution bags) {
        double tn = bags.perClassPerBag(0, 0);
        double fp = bags.perClassPerBag(0, 1);
        double fn = bags.perClassPerBag(1, 0);
        double tp = bags.perClassPerBag(1, 1);
        
        double pre1 = tp/(tp + fp);
        double rec1 = tp/(tp + fn);

        double pre2 = fp/(fp + tp);
        double rec2 = fp/(fp + tn);
        
        double f11 = 2 * (pre1 * rec1)/(pre1 + rec1);
        double f12 = 2 * (pre2 * rec2)/(pre2 + rec2);
        
        if (tp == 0) pre1 = rec1 = f11 = EPS;
        if (fp == 0) pre2 = rec2 = f12 = EPS;
        
        return 1/Math.max(f11, f12);
    }
}
