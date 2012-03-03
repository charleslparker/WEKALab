package org.clparker.wekalab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.clparker.utils.Libes;

public class KnnSet extends ArrayList<DataPoint>
    implements Comparator<DataPoint> {
    
    private static final long serialVersionUID = 3305056579412094238L;
    public int K = 10;
    
    public double[][] getData() {
        double[][] data = new double[size()][get(0).data.length + 1];
        for (int i = 0; i < size(); i++) {
            data[i] = get(i).toDoubleArray();
        }
        return data;
    }
    
    public void sort(DataPoint d) {
        for (int i = 0; i < size(); i++)
            get(i).distance = d.sqDistance(get(i));
        Collections.sort(this, this);
    }

    public int compare(DataPoint arg0, DataPoint arg1) {
        if (arg0.distance < arg1.distance) return -1;
        if (arg0.distance == arg1.distance) return 0;
        return 1;
    }

    public void transformData(double[][] trans) {
        for (DataPoint d : this) {
            System.out.println("Transforming . . .");
            d.data = Libes.matrixProd(trans, d.data);
        }
    }
    
    public double[] getDist(DataPoint d) {
        // int pCount = 0;
        // int lCount = 0;
        double[] distances = new double[K];
        double[] dist = new double[2];
        // DataPoint[] points = new DataPoint[K];
        
        
        dist[0] = 0;
        dist[1] = 0;
        
        sort(d);
        
        /*
        while (pCount < K && lCount < size()) {
            DataPoint point = get(lCount);
            boolean inArray = false;
            for (int i = 0; i < pCount; i++)
                if (point.id.fileId == points[i].id.fileId)
                    inArray = true;
            
            if (!inArray) {
                points[pCount] = point;
                distances[pCount] = 1/Math.sqrt(point.distance);
                pCount++;
            }
            lCount++;
        }

        /*
        ///  Output test:
        for (int i = 0; i < K; i++) {
            double db = 1/Math.sqrt(get(i).distance);
            double da = distances[i];
            int fb = get(i).id.fileId;
            int fa = points[i].id.fileId;
            
            if (d.pointClass)
                System.out.println("PC: " + d.pointClass + " Before: " + fb + ", " + db + " -- After: " + fa + ", " + da);
        	
        }
        System.out.println("*****************");
        ///  End test
		*/
        
        for (int i = 0; i < K; i++) {
            // System.out.println(get(i).id.fileId);
            distances[i] = 1/Math.sqrt(get(i).distance);
        }
        
        double sum = Libes.sum(distances);
        
        for (int i = 0; i < K; i++) {
            if (get(i).pointClass) dist[1] += distances[i]/sum;
            // if (points[i].pointClass) dist[1] += distances[i]/sum;
            else dist[0] += distances[i]/sum;
        }
        return dist;
    }
    
    public double[] getDist(double[] data) {
        DataPoint d = new DataPoint(data, false);
        return getDist(d);
    }
    
    public static void main(String[] args) {
        /*
        double[][] matrix = new double[4][4];
        
        double[] a = {1, 2, 3, 4};
        double[] b = {5, 6, 7, 8};
        double[] c = {9, 10, 11, 12};
        double[] d = {13, 14, 15, 16};

        matrix[0] = a;
        matrix[1] = b;
        matrix[2] = c;
        matrix[3] = d;

        double[] vec = {3, 4, 5, 6};
        
        System.out.println(Libes.toString(Libes.matrixProd(matrix, vec)));
        */
    }
}
