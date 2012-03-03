package org.clparker.wekalab;

import org.clparker.utils.Libes;

public class DataPoint implements Comparable<DataPoint>{
    public static final double DIST_EPS = 0.001;
	public static int sortKey = -1;
    
    public double[] data;
    public double distance;
    public boolean pointClass;
    public PointId id;
    
    public DataPoint(double[] newData, int clsInd) {
        data = new double[newData.length - 1];
        int count = 0;
        for (int i = 0; i < newData.length; i++) {
            if (i != clsInd) data[count++] = newData[i];
        }
        if (newData[clsInd] == 0) pointClass = false;
        else pointClass = true;
        distance = 0;
    }
    
    public DataPoint(double[] newData, boolean cls) {
        data = newData;
        pointClass = cls;
        distance = 0;
    }
    
    public double[] toDoubleArray() {
        double[] d = new double[data.length + 1];
        for (int i = 0; i < data.length; i++) d[i] = data[i];
        if (pointClass) d[data.length] = 1;
        else d[data.length] = 0;
        return d;
    }
    
    public int compareTo(DataPoint arg0) {
        if (data[sortKey] > arg0.data[sortKey]) return 1;
        if (data[sortKey] == arg0.data[sortKey]) return 0;
        return -1;
    }
    
    public double sqDistance(DataPoint arg0) {
        if (arg0.id != null && id != null &&
            arg0.id.fileId == id.fileId) return Double.MAX_VALUE;
        double[] diff = Libes.diff(arg0.data, data);
        double dist = Libes.dotProd(diff, diff);
        if (dist == 0) {
            dist = DIST_EPS;
        }
        return dist;
    }
    
    
}
