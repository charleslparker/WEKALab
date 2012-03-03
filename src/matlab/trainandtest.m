function [acc preds confmat model] = trainandtest(train, test, type, classattr, opts, intopts)
    %TRAINANDTEST train and test a classifier
    %   TRAINANDTEST(train, test, type, classattr, opts) learns a 
    %   classifier over the training data and uses the points in the 
    %   dataset test to test the learned classifier.  Both the results of 
    %   the test and the learned model are returned.
    %
    %   The data should be in matrix form, with each row being a point, and
    %   the class being an integer between 0 and the number of classes - 1.  
	%   For more information, check the WekaLab readme.
    %
    %   Inputs:
    %       train - The input training data
    %       test - The data for testing the learned classifier
    %       type - The classifier type, given by listclassifiers
    %       classattr - The index of the class attribute in data 
    %       (default end)
    %       opts - An option string for the classifier (default '')
    %       intopts - An internal option string for adaboost and svms
    %       (default '')
    %  
    %   Outputs:
    %       acc - Prediction accuracy, correct/total, on the test set
    %       preds - An r-by-n vector of predictions where r is the number
    %       of rows in test and n is number of classes.  Each element
    %       represents the probability of the rth point in data belonging
    %       to class n.
    %       confmat - The confusion matrix of the predictor over the given 
    %       data, with predicted class on the x-axis and true class 
    %       on the y-axis.
    %       model - The model learned over the training set.    
    
    if nargin < 4
        classattr = cols(train);
    end
    if nargin < 5
        opts = '';
    end
    if nargin < 6
        intopts = '';
    end

    if cols(train) == 0
        error('Empty training set!');
    end
    
    if cols(test) == 0
        error('Empty test set!');
    end

    if cols(train) ~= cols(test)
        error('Unequal numbers of columns in training and test sets!');
    end
    
    %fprintf('Training model on training data . . . ');
    model = trainclassifier(train, type, classattr, opts, intopts);
    %fprintf('done!\n');
    
    nclasses = model.getNumClasses();
    confmat = zeros(nclasses, nclasses);
    
    %fprintf('Evaluating . . . ');
    preds = getclassdistribution(model, test);
    %fprintf('done!\n');

    [vals, cls] = max(preds, [], 2);
    trueclasses = test(:,classattr) + 1;
    
    for i = 1:rows(cls)
        x = cls(i);
        y = trueclasses(i);
        confmat(x, y) = confmat(x, y) + 1;
    end
    acc = sum(diag(confmat))/sum(confmat(:));

end