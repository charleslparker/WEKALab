package org.clparker.wekalab;
public class PointId {
    int fileId = Integer.MIN_VALUE;
    int seqId = Integer.MIN_VALUE;
    
    public PointId(int f, int s) {
        fileId = f;
        seqId = s;
    }
}
