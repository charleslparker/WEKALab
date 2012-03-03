    %GETMODEL transforms a classifier name to an integer
    %   GETMODEL transforms the string name of a classifier to
	%	an integer understandable within the WEKALab java
	%	interfaces.  The integers are hard-coded within
	%	WekaInterface.java.  Any changes must be made in
	%	both locations.
function model = getmodel(modstr)
	if (strcmp(modstr, 'bayes'))
		model = 0;
	elseif (strcmp(modstr, 'logistic'))
		model = 1;
	elseif (strcmp(modstr, 'perceptron'))
		model = 2;
	elseif (strcmp(modstr, 'svm_poly'))
		model = 3;
	elseif (strcmp(modstr, 'svm_rbf'))
		model = 4;
	elseif (strcmp(modstr, 'knn_1'))
		model = 5;
	elseif (strcmp(modstr, 'knn_10'))
		model = 6;
	elseif (strcmp(modstr, 'tree'))
		model = 7;
	elseif (strcmp(modstr, 'adaboost_tree'))
		model = 8;
	elseif (strcmp(modstr, 'adaboost_stump'))
		model = 9;
	elseif (strcmp(modstr, 'random_forest'))
		model = 10;
	else
		error('Invalid model type!');
	end
end