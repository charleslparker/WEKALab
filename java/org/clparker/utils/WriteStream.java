package org.clparker.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteStream {
    BufferedWriter out;
    boolean stdout, fileout;
    
    private static final String DEFAULT_FILE = "C:\\WriteStream.temp";

    public WriteStream(String file) throws IOException {
        out = new BufferedWriter( new FileWriter(file) );
        fileout = stdout = true;
    }
    
    public WriteStream(String file, boolean append) throws IOException {
        out = new BufferedWriter( new FileWriter(file, append) );
        fileout = stdout = true;
    }

    public WriteStream() throws IOException {
        out = new BufferedWriter( new FileWriter(DEFAULT_FILE) );
        fileout = stdout = true;
    }

    public void flush() throws IOException {
        out.flush();
    }
    
    public void write(String s) throws IOException {
        if (stdout) System.out.print(s);
        if (fileout) out.write(s);
    }
    
    public void write(int n) throws IOException {
        if (stdout) System.out.print(n);
        if (fileout) out.write(n);
    }

    public void write(float n) throws IOException {
        if (stdout) System.out.print(n);
        if (fileout) out.write("" + n);
    }
    
    public void write(double n) throws IOException {
        if (stdout) System.out.print(n);
        if (fileout) out.write("" + n);
    }

    public void setFile(String file) throws IOException {
        if (out != null) out.close();
        out = new BufferedWriter( new FileWriter(file) );
        fileout = true;
    }
    
    public void writeBoth() { fileout = stdout = true; }
    public void fileOnly() { fileout = true;  stdout = false; }
    public void noFile() {fileout = false; stdout = true; }
    public void close() throws IOException { out.close(); fileout = false; }
}
