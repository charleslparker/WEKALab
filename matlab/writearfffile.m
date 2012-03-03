function writearfffile(data, name, classattr)
    %WRITEARFFFILE reads a file in ARFF format
    %   WRITEARFFFILE(data, 'name', classattr) 
    %   writes the data given in data to a file called name
    %   in the WEKA ARFF format.  For more about ARFF, visit the 
    %   WEKA home page.
    if nargin < 3
        classattr = cols(data);
    end
    
    in = org.clparker.wekalab.WekaInterface.createSet(data, classattr);
    org.clparker.wekalab.WekaInterface.writeSet(in, name);
end