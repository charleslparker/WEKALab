function data = readarfffile(name)
    %READARFFFILE reads a file in ARFF format
    %   READARFFFILE('filename') reads the file given in name into a 
    %   matrix of doubles. The file must be in ARFF format.  For more about 
    %   ARFF, visit the WEKA home page.
    in = org.clparker.wekalab.WekaInterface.readArffFile(name);
    data = org.clparker.wekalab.WekaInterface.setToDouble(in);
end