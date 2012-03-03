WEKALab is an interface to WEKA from the Matlab command line.  You can learn more about Weka here:

http://www.cs.waikato.ac.nz/~ml/weka/

It provides an extremely simple interface to learn classifiers while giving you the power of Matlab in terms of powerful matrix manipulations.  This is incredibly useful, as so much of the dimensionality reduction algorithms invented in recent years rely on significant matrix algebra.  As such, WEKALab allows you to combine the power of classifiers in WEKA with dimensionality reduction in Matlab.

WEKALab (version 0.2)

WEKALab is essentially just a collection of matlab scripts and java files that allow easy access to the classifiers in WEKA from matlab.  WEKA is the open source package of classifiers downloadable here:

http://www.cs.waikato.ac.nz/ml/weka/index_downloading.html

The goal is to provide a way to do some basic testing of classifiers without leaving matlab, as well as functionality to import and export the WEKA file format (ARFF) given only a data matrix.

Installation
-------------
To install WEKALab, simply copy the WEKALab m-files and jars into a directory and add it to your Matlab path.  Then, at the command prompt type

> wlsetup

You should get the message, 'wlsetup complete!'.  Indicating that entries have been made to your java class path to reference the appropriate jar files.  AT THIS POINT, YOU MUST RESTART MATLAB.  Afterwards, you can test setup by typing:

> javaclasspath

The last two entries of the static class path should be the weka and WEKALab jar files.  Also try typing:

> listclassifiers

This should give you a summary of the classifiers available to you within WEKALab, and indicates that setup has gone smoothly.


Usage
---------------
The basic workflow for Matlab is as follows:  Import or create a matrix of data (or two matricies if you have pre-defined training and testing data), run tests on this matrix, observe the results, export the matrix to WEKA format for further testing.  In your WEKALab directory, you should have an ARFF file containing the venerable 'Iris' dataset.  You can import this from the Matlab Command Line by typing:

> M = readarfffile('iris.arff')

The double matrix M now contains the data in the file, with rows as data points and columns as features (or "attributes" in WEKA parlance).  Importantly, the text-based class names in the raw file have been transformed into a sequence of integers from 0 to number of classes - 1.  This is important as all WEKALab data must be in this format.

Note here that YOU DON'T HAVE TO READ IN DATA FROM AN ARFF FILE.  You could create this matrix yourself within matlab and things would work just as well.  This is not a special data structure, just a double matrix.  This is the whole point of WEKALab:  It works fine with ordinary Matlab matrices.

A simple test to do here is 10 fold cross-validation using, say, decision tress:

> [a c p m] = crossvalidate(M, 'tree', 10)

The output variables are, in order, the overall accuracy on the set, an n x n "confusion matrix" where n is the number of classes, an n x r matrix of predictions on each point where r is the number of points, and the model learned on the entire dataset.

The point-wise prediction vector, p, is something not available in the GUI that comes with WEKA, and proves very handy if you want a breakdown of exactly which points you've gotten wrong.  Each row in p has n columns, and each column is the probability of that point in the input matrix belonging to the corresponding class according to the model, so the first column in the jth row of p is the probability that the point M(j,:) belongs to class 0, the second column to class 1, etc.

Also note that the function does not randomize your data!  If you wish the data to be randomized, you must do it yourself beforehand!

To train a model on your dataset without testing it, do:

> C = trainclassifier(M, 'tree');

If you wish to classify a point, define the point with the same number of colums as the input data and then use getclassdistribution:

> k = [0 0 0 0 0];
> getclassdistribution(C, k)

Note that getclassdistribution also works for matrices:

> getclassdistribution(C, M)

The important thing, again, is that the matrix have the same number of columns as the training data.  The values you use for the classes, obviously, can be dummy values, but they must be there.

Finally, if you want to output your matrix to ARFF format:

> writearfffile(M, 'iris.test')

Note that the text class labels are not recovered (they are still integers) and there is a line at the very beginning of the file '%@SIZE'. . .  This is for some of my own C code that reads this data into a data structure and needs to preallocate.  WEKA itself ignores lines beginning with '%', and so this file should read fine into WEKA.


Extensions
--------------------
To mess with the classifier options within WEKA, pass in an options string at training time.  For more information on how this works use:

> help listoptions

Or, for example:

> listoptions('svm_rbf')

Some classifiers, like svm_rbf, have their default options overridden by WEKALab (in this case, to select the RBF kernel).

For 'svm_rbf' and 'adaboost_tree' among others, you can pass in a second "internal options" string, that modifies the parameters of the weak classifier in the case of adaboost, and the parameters of the kernel in the case of svm_rbf.  You can again see these by using list options.

To extend WekaLab to support more classifiers, or different sets of default options (e.g., a different default selection for 'k' in k-nearest neighbor), you need to modify WekaInterface.java, recompile, and reassemble WEKALab.jar.  This modification should be pretty straightforward, though, if you have decent access to the documentation.