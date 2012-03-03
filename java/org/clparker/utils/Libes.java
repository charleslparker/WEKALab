package org.clparker.utils;

import java.util.Collections;
import java.util.Random;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Libes {
    public static String home = "";
    public static String seperator = "\\";
	public static Random random = new Random();
    
	public static void setSeed(int r) {
		random.setSeed(r);
	}

	public static ArrayList<Integer> getFold(ArrayList<Integer> a, int numFolds, int fold, boolean inFold) {
	    ArrayList<Integer> list = new ArrayList<Integer>();
	    int bottom = (int)Math.round((double)fold * a.size()/numFolds);
	    int top = (int)Math.round((double)(fold + 1) * a.size()/numFolds);
	    for (int i = 0; i < a.size(); i++) {
	        if ((i >= bottom && i < top) == inFold) list.add(a.get(i));
	    }
	    return list;
	}
	public static double[][] readMatrix(String file, String delim) {
	    Reader r = new Reader(file);
	    int pointCount = new StringTokenizer(r.readLine(), delim).countTokens();
	    int lineCount = 1;
	    
	    
	    while (r.ready())
	        if (r.readLine().length() > 1) lineCount++;

	    double[][] m = new double[lineCount][pointCount];
	    r.close();
	    r = new Reader("testdata.txt");
	    int i = 0;
        while (r.ready()) {
            int j = 0;
            StringTokenizer t = new StringTokenizer(r.readLine());
            while (t.hasMoreTokens()) 
                m[i][j++] = Double.parseDouble(t.nextToken());
            i++;
        }
	    return m;
	}
	
    public static double[][] readMatrix(String file) {
        return readMatrix(file, " \t");
    }

    public static double random() {
    	return random.nextDouble();
    }
    
    public static double[][] deepCopy(double[][] x) {
        double[][] d = new double[x.length][x[0].length];
        for (int i = 0; i < x.length; i++)
            for (int j = 0; j < x[0].length; j++) d[i][j] = x[i][j];
        return d;
    }
    
    public static boolean[][] deepCopy(boolean[][] x) {
        boolean[][] b = new boolean[x.length][x[0].length];
        for (int i = 0; i < x.length; i++)
            for (int j = 0; j < x[0].length; j++) b[i][j] = x[i][j];
        return b;
    }

    public static double[] stringToDouble(String s, String delim) {
    	StringTokenizer t = new StringTokenizer(s, delim);
    	double[] d = new double[t.countTokens()];
    	int i = 0;
    	while (t.hasMoreTokens()) d[i++] = Double.parseDouble(t.nextToken());
    	return d;
    }

	public static double avg(double [] vect) {
		double sum = 0;
        for (int i = 0; i < vect.length; i++) sum += vect[i];
        return sum/vect.length;
    }
    
    public static double stDev(double[] vect) {
    	double avg = avg(vect);
    	double stDev = 0;
        for (int i = 0; i < vect.length; i++) {
        	double sqDiff = (vect[i] - avg)*(vect[i] - avg);
            stDev += sqDiff;
        }
        stDev /= vect.length;
        return (double)(Math.sqrt(stDev));
     }

    public static double min(double[][] array) {
    	double min = Float.MAX_VALUE;
    	for (int i = 0; i < array.length; i++) {
    		for (int j = 0; j < array[0].length; j++) {
    			if (array[i][j] < min && array[i][j] != 0) min = array[i][j];
    		}
    	}
    	return min;
    }

    public static int[] maxInd(double[][] array) {
        double max = Float.MIN_VALUE;
        int[] ind = {-1, -1};
        
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] > max) {
                    max = array[i][j];
                    ind[0] = i;
                    ind[1] = j;
                }
            }
        }
        return ind;
    }
    
    public static double max(double[][] array) {
    	double max = Float.MIN_VALUE;
    	for (int i = 0; i < array.length; i++) {
    		for (int j = 0; j < array[0].length; j++) {
    			if (array[i][j] > max) max = array[i][j];
    		}
    	}
    	return max;
    }
    
    public static int[] toInt(ArrayList<Integer> a) {
        int[] array = new int[a.size()];
        for (int i = 0; i < a.size(); i++) array[i] = a.get(i);
        return array;
    }
    
    public static double max(double[] array) {
    	double max = Double.NEGATIVE_INFINITY;
    	for (int i = 0; i < array.length; i++) {
    		if (array[i] > max) max = array[i];
    	}
    	return max;
    }

    public static double maxInColumn(double[][] array, int col) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < array.length; i++) {
            if (array[i][col] > max) max = array[i][col];
        }
        return max;
    }
    
    public static int maxInd(double[] array) {
        double max = Double.NEGATIVE_INFINITY;
        int maxInd = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxInd = i;
            }
        }
        return maxInd;
    }
    
    public static double min(double[] array) {
    	double min = Double.POSITIVE_INFINITY;
    	for (int i = 0; i < array.length; i++) {
    		if (array[i] < min) min = array[i];
    	}
    	return min;
    }

    public static double range(double[] array) {
    	double max = max(array);
    	double min = min(array);
    	return Math.abs(max - min);
    }

    public static double log2(double n) {
    	return (double)(Math.log(n)/Math.log(2));
    }
    
    public static double[] log(double[] x) {
        double[] logX = new double[x.length];
        for (int i = 0; i < x.length; i++) logX[i] = Math.log(x[i]);
        return logX;
    }
    
    public static String toSparseString(double[] d, int offset) {
    	StringBuffer s = new StringBuffer("");
    	for (int i = 0; i < d.length; i++) 
    		if (d[i] != 0) s.append(" " + (i + offset) + ":" + d[i]);
    	return s.toString();
    }

    public static String toString(double[] d) {
    	StringBuffer s = new StringBuffer("");
    	for (int i = 0; i < d.length; i++) s.append(d[i] + " ");
    	return s.toString();
    }

    public static String toString(double[] d, String delim) {
    	StringBuffer s = new StringBuffer("");
    	for (int i = 0; i < d.length - 1; i++) s.append(d[i] + delim);
    	s.append(d[d.length - 1]);
    	return s.toString();
    }
    
    public static double sum(double[] x) {
    	double sum = 0;
    	for (int i = 0; i < x.length; i++) {
    	    if (!Double.isInfinite(x[i]) && !Double.isNaN(x[i])) 
    	        sum += x[i];
    	}
    	return sum;
    }
	
    public static boolean find(double[] x, double val) {
        for (int i = 0; i < x.length; i++) {
            if (x[i] == val) return true;
        }
        return false;
    }
    
	public static double dotProd(double[] x, double[] y) {
		if (x.length != y.length) {
			System.out.println("Warning: Unequal Length Vectors in dotProd: " + x.length + ", " + y.length);
			return 0;
		}
		double prod = 0;
		for (int i = 0; i < x.length; i++) prod += x[i]*y[i];
		return prod;
	}
	
    public static double[][] outerProd(double[] x, double[] y) {
        double[][] prod = new double[x.length][y.length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < y.length; j++) { 
                prod[i][j] = x[i] * y[j];
            }
        }
        return prod;
    }
    
    public static double[] diff(double[] x, double[] y) {
		double[] diff = new double[x.length];
		for (int i = 0; i < x.length; i++) diff[i] = x[i] - y[i];
		return diff;
	}

	
	public static double[] recip(double[] x) {
		double[] recip = new double[x.length];
		for (int i = 0; i < x.length; i++) {
			recip[i] = 1/x[i];
		}
		return recip;
	}

	public static double[] sum(double[] x, double[] y) {
		double[] sum = new double[x.length];
		for (int i = 0; i < x.length; i++) sum[i] = x[i] + y[i];
		return sum;
	}
	
    public static double[][] sum(double[][] x, double[][] y) {
        if (x.length == 0 || y.length != x.length || 
            x[0].length != y[0].length) {
            System.err.println("Warning:  Unequal size matricies in Libes.sum!");
            return null;
        }
        double[][] sum = new double[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                sum[i][j] = x[i][j] + y[i][j];
            }
        }
        return sum;
    }
    
    public static int[][] sum(int[][] x, int[][] y) {
        if (x.length == 0 || y.length != x.length || 
            x[0].length != y[0].length) {
            System.err.println("Warning:  Unequal size matricies in Libes.sum!");
            return null;
        }
        int[][] sum = new int[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                sum[i][j] = x[i][j] + y[i][j];
            }
        }
        return sum;
    }

    public static double[] prod(double[] x, double[] y) {
        double[] prod = new double[x.length];
        for (int i = 0; i < x.length; i++) prod[i] = x[i] * y[i];
        return prod;
    }

    public static double[] quot(double[] x, double[] y) {
        double[] prod = new double[x.length];
        for (int i = 0; i < x.length; i++) prod[i] = x[i] / y[i];
        return prod;
    }

	public static double[] sq(double[] x) {
		double[] prod = new double[x.length];
		for (int i = 0; i < x.length; i++) prod[i] = x[i] * x[i];
		return prod;
	}
	
	public static double[] row(double[][] x, int row) {
		double[] vec = new double[x[row].length];
		for (int i = 0; i < vec.length; i++)
			vec[i] = x[row][i];
		return vec;
	}
	
	public static double[] col(double[][] x, int col) {
		double[] vec = new double[x.length];
		for (int i = 0; i < vec.length; i++)
			vec[i] = x[i][col];
		return vec;
	}

	public static double[][] sq(double[][] x) {
		double[][] prod = new double[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				prod[i][j] = dotProd(row(x, i), col(x, j));
			}
		}
		return prod;
	}

	public static double getNorm(double[] x) {
		return Math.sqrt(dotProd(x, x));
	}
	
	public static double[] normalize(double[] x) {
		double norm = getNorm(x);
		double[] newVec = new double[x.length];
		for (int i = 0; i < x.length; i++) newVec[i] = x[i]/norm;
		return newVec;
	}

    public static double[] probNormalize(double[] x) {
        double sumVec = sum(x);
        double[] newVec = new double[x.length];
        for (int i = 0; i < x.length; i++) newVec[i] = x[i]/sumVec;
        return newVec;
    }
    
    public static double[] dataNormalize(double[] x, double[] mean, double[] std) {
        double[] newData = new double[x.length];
        for (int i = 0; i < x.length; i++)
            newData[i] = (x[i] - mean[i])/std[i];
        return newData;
    }
    
    public static double[] scale(double[] x, double s) {
		double[] newVec = new double[x.length];
		for (int i = 0; i < x.length; i++) newVec[i] = x[i]*s;
		return newVec;
	}

    public static double[][] scale(double[][] x, double s) {
        double[][] newVec = new double[x.length][x.length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                newVec[i][j] = x[i][j]*s;
            }
        }
        return newVec;
    }

    public static double[] abs(double[] x) {
		double[] newVec = new double[x.length];
		for (int i = 0; i < x.length; i++) newVec[i] = Math.abs(x[i]);
		return newVec;
	}
	
	public static double[] matrixProd(double[][] x, double[] y) {
		if (x[0].length != y.length) return null;
		double[] vec = new double[x.length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[i].length; j++) {
				vec[i] += x[i][j]*y[j];
			}
		}
		return vec;
	}

	public static double[] matrixProd(double[] y, double[][] x) {
		if (x.length != y.length) return null;
		double[] vec = new double[x[0].length];
		for (int i = 0; i < x[0].length; i++) {
			for (int j = 0; j < x.length; j++) {
				vec[i] += x[j][i]*y[j];
			}
		}
		return vec;
	}
	
	public static int[] randPerm(int n) {
	    ArrayList<Integer> vec = new ArrayList<Integer>();
	    for (int i = 0; i < n; i++) vec.add(i);
	    Collections.shuffle(vec);
	    int[] array = new int[n];
        for (int i = 0; i < n; i++) array[i] = vec.get(i);
        return array;
	}
	
	public static void main(String[] args) {
        return;
	}
	
}
