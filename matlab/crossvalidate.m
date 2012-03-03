function [acc preds confmat model] = crossvalidate(data, type, folds, classattr, opts, intopts)
    %CROSSVALIDATE run n-fold cross-validation
    %   CROSSVALIDATE(data, type, folds, classattr, opts, intopts)
    %   runs n-fold cross-validation on the input data given 
    %   in the first parameter.  Note that folds are taken sequentially from
	%	the data, YOU MUST PRE-RANDOMIZE THE DATA IF YOU WANT IT RANDOMIZED.
    %
    %   The data should be in matrix form, with each row being a point, and
    %   the class being an integer between 0 and the number of classes - 1.  
	%   For more information, check the WekaLab readme.
	%
    %   Inputs:
    %       data - The input data
    %       type - The classifier type, given by listclassifiers
    %       fold - The number of folds for cross validation 
    %       (default 10)
    %       classattr - The index of the class attribute in data 
    %       (default end)
    %       opts - An option string for the classifier (default '')
    %       intopts - An internal option string for adaboost and svms
    %       (default '')
    %  
    %   Outputs:
    %       acc - Prediction accuracy, correct/total
    %       preds - An r-by-n vector of predictions where r is the number
    %       of rows in data and n is number of classes.  Each element
    %       represents the probability of the rth point in data belonging
    %       to class n.
    %       confmat - The confusion matrix of the predictor over the given 
    %       data, with predicted class on the y-axis and true class 
    %       on the x-axis.
    %       model - The model learned over all of the training data.
    
    if nargin < 3
        folds = 10;
    end
    
    if nargin < 4
        classattr = cols(data);
    end
    if nargin < 5
        opts = '';
    end
    if nargin < 6
        intopts = '';
    end
    
    if cols(data) == 0
        error('Empty training set!');
    end
	
	if (rows(data) < folds)
		error('Less data points than folds!');
	end

    % fprintf('Training model on all training data . . . ');
    model = trainclassifier(data, type, classattr, opts, intopts);
    % fprintf('done!\n');
    
    nclasses = model.getNumClasses();
    confmat = zeros(nclasses, nclasses);
    preds = zeros(rows(data), nclasses);
    testrow = 1;
    
    for i = 1:folds
        [train test] = getsplit(data, i, folds);
        % fprintf('Training model on fold %d . . . ', i);
        tempmod = trainclassifier(train, type, classattr, opts, intopts);
        % fprintf('evaluating . . . ');
        preds(testrow:testrow + rows(test) - 1,:) = getclassdistribution(tempmod, test);
        % fprintf('done!\n');
        testrow = testrow + rows(test);
    end
    [vals, cls] = max(preds, [], 2);
    trueclasses = data(:,classattr) + 1;
    
    for i = 1:rows(cls)
        x = cls(i);
        y = trueclasses(i);
        confmat(x, y) = confmat(x, y) + 1;
    end
    acc = sum(diag(confmat))/sum(confmat(:));
end