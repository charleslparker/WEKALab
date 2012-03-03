function [acc preds confmat model] = idcv(data, ids, type, folds, classattr, opts, intopts)
    %IDCROSSVALIDATE run n-fold cross-validation based on given ids
    %   CROSSVALIDATE(data, ids, type, folds, classattr, opts, intopts)
    %   runs n-fold cross-validation on the input data given 
    %   in the first parameter.
    %
    %   The data should be in matrix form, with each row being a point, and
    %   the class being an integer.  For more information, check the
    %   WekaLab readme.
    %
    %   Inputs:
    %       data - The input data
    %       ids - Possibly overlapping vector of ids for input data.
    %       When cross validation occurs, datapoints with the same id
    %       never appear in both the training and test sets.
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
    
    fprintf('Training...');
    if nargin < 4
        folds = 10;
    end
    
    if nargin < 5
        classattr = cols(data);
    end
    if nargin < 6
        opts = '';
    end
    if nargin < 7
        intopts = '';
    end
    
    if cols(data) == 0
        error('Empty training set!');
    end

    % fprintf('Training model on all training data . . . ');
    model = trainclassifier(data, type, classattr, opts, intopts);
    % fprintf('done!\n');
    
    nclasses = model.getNumClasses();
    confmat = zeros(nclasses, nclasses);
    preds = zeros(rows(data), nclasses);
    testrow = 1;
    uids = unique(ids);
    uids = uids(randperm(rows(uids)));
    
    for i = 1:folds
        [trids teids] = getsplit(uids, i, folds);
        train = data(inarray(ids, trids), :);
        test = data(inarray(ids, teids), :);
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