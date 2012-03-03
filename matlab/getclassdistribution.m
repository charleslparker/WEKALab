function dist = getclassdistribution(classifier, data)
    %GETCLASSDISTRIBUTION uses a classifier to classify data
    %   GETCLASSDISTRIBUTION uses the given classifier to make class
    %   predictions about the data given in data.  This data must be of the
    %   same form as the data used to construct the classifier.
    %   Specifically, it must have the same number of columns, and the same
    %   column must contain the class variable.
    %
    %   The data should be in matrix form, with each row being a point, and
    %   the class being an integer between 0 and the number of classes - 1.  
	%   For more information, check the WekaLab readme.
    %
    %   Inputs:
    %       classifier - a model learned using TRAINCLASSIFIER
    %       data - the data to be classified by the model
    %  
    %   Outputs:
    %       dist - An r-by-n vector of predictions where r is the number
    %       of rows in data and n is number of classes.  Each element
    %       represents the probability of the rth point in data belonging
    %       to class n.

    if (isempty(data) || isempty(classifier))
        dist = [];
        return
    end
    
    if cols(data) == classifier.getNumAttributes() - 1
        data = [data zeros(rows(data),1)];
    end
    
    if cols(data) ~= classifier.getNumAttributes()
        error('Incorrect number of columns in data!');
    end
        
    numcls = classifier.getNumClasses();
    dist = zeros(rows(data), numcls);
    
    for i = 1:rows(data)
        cl = classifier.getClassDistribution(data(i,:))';
        dist(i,:) = cl;
    end
end