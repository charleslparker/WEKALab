function c = trainclassifier(train, type, classattr, opts, intopts)
    %TRAINCLASSIFIER learn a classifier over the given data
    %   TRAINCLASSIFIER learns a classifier over the given training data 
    %
    %   The data should be in matrix form, with each row being a point, and
    %   the class being an integer between 0 and the number of classes - 1.  
	%   For more information, check the WekaLab readme.
    %
    %   Inputs:
    %       train - The input training data
    %       type - The classifier type, given by listclassifiers
    %       classattr - The index of the class attribute in data 
    %       (default end)
    %       opts - An option string for the classifier (default '')
    %       intopts - An internal option string for adaboost and svms
    %       (default '')
    %  
    %   Outputs:
    %       c - The model learned over the training set.    

    if nargin < 2
        fprintf('Must have at least two arguments!\n');
        return
    end
    if nargin < 3
        classattr = cols(train);
    end
    if nargin < 4
        opts = '';
    end
    if nargin < 5
        intopts = '';
    end
    
	ntype = getmodel(type);
    tset = org.clparker.wekalab.WekaInterface.createSet(train, classattr);
    c = org.clparker.wekalab.WekaModel(tset, ntype, classattr, opts, intopts);
end