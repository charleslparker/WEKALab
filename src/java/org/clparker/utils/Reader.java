package org.clparker.utils;

import java.io.*;

public class Reader {
    BufferedReader b;

    public Reader(String s) {
        try {
            b = new BufferedReader( new FileReader(s) );
        }
        catch (IOException e) {
            System.out.println("Exception in Reader");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    public Reader(File f) {
        try {
            b = new BufferedReader( new FileReader(f) );
        }
        catch (IOException e) {
            System.out.println("Exception in Reader");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    public Reader(File f, int len) {
        try {
            b = new BufferedReader( new FileReader(f), len );
        }
        catch (IOException e) {
            System.out.println("Exception in Reader");
            e.printStackTrace();
            System.exit(-1);
        }
    }
        
    public Reader() {
        b = new BufferedReader( new InputStreamReader(System.in) );
    }
    
    public String readLine() {
        try {
            return b.readLine();
        }
        catch (IOException e) {
            System.out.println("Exception in Reader.readLine");
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
    
    public void close() {
        try {
            b.close();
        }
        catch (IOException e) {
            System.out.println("Exception in Reader.close");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    public boolean ready() {
        try {
            return b.ready();
        }
        catch (IOException e) {
            System.out.println("Exception in Reader.ready");
            e.printStackTrace();
            System.exit(-1);
        }
        return false;
    }
    
    public void mark(int r) {
        try {
            b.mark(r);
        }
        catch (IOException e) {
            System.out.println("Exception in Reader.mark");
            e.printStackTrace();
        }
   }
    
    public void reset() {
        try {
            b.reset();
        }
        catch (IOException e) {
            System.out.println("Exception in Reader.reset");
            e.printStackTrace();
        }
    }

    public void skip(long n) {
        try {
            b.skip(n);
        }
        catch (IOException e) {
            System.out.println("Exception in Reader.skip");
            e.printStackTrace();
        }
    }

    public int read() {
        try {
            return b.read();
        }
        catch (IOException e) {
            System.out.println("Exception in Reader.read");
            e.printStackTrace();
        }
        return -2;
    }
    
    public int read(char[] buf) {
        try {
            return b.read(buf);
        }
        catch (IOException e) {
            System.out.println("Exception in Reader.read");
            e.printStackTrace();
        }
        return -2;
    }
}
