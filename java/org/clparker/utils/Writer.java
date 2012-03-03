package org.clparker.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;

public class Writer {
    private WriteStream fout;
    private int fieldLength = 7;
    private static DecimalFormat form = new DecimalFormat("###0.0000;-###0.0000");
    
    public Writer(String file) {
        try {
            fout = new WriteStream(Libes.home + file);
        }
        catch (IOException e) {
            System.out.println("Exception in Writer");
            e.printStackTrace();
        }
    }
    
    public Writer(String file, boolean append) {
        try {
            fout = new WriteStream(Libes.home + file, append);
        }
        catch (IOException e) {
            System.out.println("Exception in Writer");
            e.printStackTrace();
        }
    }

    public Writer() {
        try {
            fout = new WriteStream();
            fout.noFile();
        }
        catch (IOException e) {
            System.out.println("Exception in Writer");
            e.printStackTrace();
        }    
    }
    
    public void setFile(String file) {
        try {
            fout.setFile(Libes.home + file);
        }
        catch (IOException e) {
            System.out.println("Exception in Writer");
            e.printStackTrace();
        }
    }
    
    public void writeBoth() { fout.writeBoth(); }
    public void fileOnly() { fout.fileOnly(); }
    public void noFile() { fout.noFile(); }
    
    public void setField(int f) {
        fieldLength = f;
    }
    
    public void writeFixed(String s) {
        write(printFixed(s, fieldLength));
    }
    
    public void writeFixed(float d) {
        //write(printFixed(d, fieldLength));
    	write(d + " ");
    }
    
    public void writeFixed(int i) {
        write(printFixed(i, fieldLength));
    }
    
    public void writeFixed(boolean b) {
        write(printFixed(b, fieldLength));
    }
    
    public void write(float[] d) {
        for (int i = 0; i < d.length; i++) writeFixed(d[i]);
    }

    public void write(int[] n) {
        for (int i = 0; i < n.length; i++) writeFixed(n[i]);
    }
    
    public void writeLine(float[] d) {
        write(d);
        write("\n");
    }
    
    public void writeLine(int[] n) {
        write(n);
        write("\n");
    }
    
    public void write(float[][] d) {
        for (int i = 0; i < d.length; i++) writeLine(d[i]);
    }
    
    public void write(String s) {
        try {
            fout.write(s);
        }
        catch (IOException e) {
            System.out.println("Exception in Writer.write");
            e.printStackTrace();
        }
    }

    public void write(double d) {
        try {
            fout.write(d);
        }
        catch (IOException e) {
            System.out.println("Exception in Writer.write");
            e.printStackTrace();
        }
    }

    public void write(int c) {
        try {
            fout.write(c);
        }
        catch (IOException e) {
            System.out.println("Exception in Writer.write");
            e.printStackTrace();
        }
    }

    public void write(float c) {
        try {
            fout.write(c);
        }
        catch (IOException e) {
            System.out.println("Exception in Writer.write");
            e.printStackTrace();
        }
    }

    public void write(byte c) {
        try {
            fout.write(c);
        }
        catch (IOException e) {
            System.out.println("Exception in Writer.write");
            e.printStackTrace();
        }
    }
    
    public void flush() {
        try {
            fout.flush();
        }
        catch (IOException e) {
            System.out.println("Exception in Writer.write");
            e.printStackTrace();
        }
    }
        
    public void writeLine(String s) {
        try {
            fout.write(s + "\n");
        }
        catch (IOException e) {
            System.out.println("Exception in Writer.writeLine");
            e.printStackTrace();
        }
    }
    
    public void close() {
        try {
            fout.close();
        }
        catch (IOException e) {
            System.out.println("Exception in Writer.close");
            e.printStackTrace();
        }
    }
    
    public static String printFixed(float d, int n) {
        return cutToSize(form.format(d), n);
    }
    
    public static String printFixed(int v, int n) {
        return cutToSize("" + v, n);
    }

    public static String printFixed(boolean b, int n) {
        if (b) return printFixed(1, n);
        return printFixed(0, n);
    }
    
    public static String printFixed(String s, int n) {
        return cutToSize(s, n);
    }

    private static String cutToSize(String s, int n) {
        String res = s;
        if (s.length() < n) {
            for (int i = 0; i < n - s.length(); i++) {
                res = res + " ";
            }
        }
        if (s.length() >= n) return s.substring(0, n);
        return res;
    }
    
    public static void writeObject(Serializable s, String fout) {
        try {
            //use buffering
            OutputStream file = new FileOutputStream(fout);
            OutputStream buffer = new BufferedOutputStream( file );
            ObjectOutput output = new ObjectOutputStream( buffer );
            try {
                output.writeObject(s);
            }
            finally {
                output.close();
            }
        }  
        catch(IOException ex) {
            System.err.println("Exception in writeObject!");
            ex.printStackTrace();
        }
    }

    public static Object readObject(String fin) {
        Object data = null;
        try {
            //use buffering
            InputStream file = new FileInputStream( "quarks.ser" );
            InputStream buffer = new BufferedInputStream( file );
            ObjectInput input = new ObjectInputStream ( buffer );
            try {
                data = input.readObject();
            }
            finally {
                input.close();
            }
        }
        catch(ClassNotFoundException ex){
            System.err.println("Exception in writeObject!");
            ex.printStackTrace();
        }
        catch(IOException ex){
            System.err.println("Exception in writeObject!");
            ex.printStackTrace();
        }
        return data;
    }  
}
        
