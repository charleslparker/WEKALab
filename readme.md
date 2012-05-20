WEKALab is an interface to WEKA from the Matlab command line.  You can learn more about Weka here:

http://www.cs.waikato.ac.nz/~ml/weka/

It provides an extremely simple interface to learn classifiers while giving you the power of Matlab in terms of powerful matrix manipulations.  This is incredibly useful, as so much of the dimensionality reduction algorithms invented in recent years rely on significant matrix algebra.  As such, WEKALab allows you to combine the power of classifiers in WEKA with dimensionality reduction in Matlab.

The goal is to provide a way to do some basic testing of classifiers without leaving matlab, as well as functionality to import and export the WEKA file format (ARFF) given only a data matrix.

## Installation

To install WEKALab, you must have the built jar file in the appropriate place in this repo directory.  You can get that jar either by downloading it, or building it with Leiningen.

### Downloading the Jar

You may just be able to use the jar file that I use without rebuilding.  To do this, download the latest (or only) build at:

https://github.com/charleslparker/WEKALab/downloads

Simply add this file to the root of this repo and you should be all set.

### Building the Jar

WEKALab is a [Leiningen](https://github.com/technomancy/leiningen) project (in anticipation of interfacing to Clojure someday).  To build it, you'll need to install Leiningen.  For Mac, the easiest thing I've found is to install and use homebrew by typing `brew install leiningen`.  I'm sure Linux variants all have the ability to install it as a package.  For Windows, there's usually a package available for download here:

https://github.com/technomancy/leiningen/downloads

Make sure that you can run `javac` from the command line, as Leiningen will fail otherwise.  Once you've got Leiningen in place, build the WEKALab jars by typing, in this repo's directory (not in Matlab, at the OS command line):

```
$ lein deps
$ lein uberjar
```

You should see two jar files in the repo root after running these commands, one of which should be `wekalab.jar`.

### Installing in Matlab

To install WEKALab for use with Matlab, simply add `src/matlab` to your Matlab path, then type (at the Matlab command prompt):

```
>> wlsetup
```

You should get the message, `wlsetup complete!`, indicating that entries have been made to your java class path to reference the appropriate jar files.  You may also get a message indicating that wlsetup couldn't find wekalab.jar in the right place.  Put the jar file where it is expected and try again.

If you get the following message:

```
>> wlsetup
??? Error using ==> fprintf
Invalid file identifier.  Use fopen to generate a valid file identifier.

Error in ==> wlsetup at 17
    fprintf(fout, '%s\n', wljar);
```

It is probably because you don't have permission to modify classpath.txt.  **In Windows 7, you may have to be running Matlab as an administrator to get this to work**, otherwise, you'll have to talk to your systems admin.

**At this point, you must restart Matlab**.  Afterwards, you can test setup by typing:

```
>> javaclasspath
```

The last two entries of the static class path should be the weka and WEKALab jar files.

## Usage

Begin by typing, at the Matlab command prompt:

```
>> listclassifiers
```

This should give you a summary of the classifiers available to you within WEKALab, and indicates that setup has gone smoothly.

The basic workflow for WEKALab is as follows:  Import or create a matrix of data (or two matricies if you have pre-defined training and testing data), run tests on this matrix, observe the results, export the matrix to WEKA format for further testing.  In your WEKALab directory, you should have an ARFF file containing the venerable 'Iris' dataset.  You can import this from the Matlab Command Line by typing:

```
>> M = readarfffile('test/iris.arff')
```

The double matrix M now contains the data in the file, with rows as data points and columns as features (or "attributes" in WEKA parlance).  Importantly, the text-based class names in the raw file have been transformed into a sequence of integers from 0 to number of classes - 1.  This is important as all WEKALab data must be in this format.

Note here that **you don't have to read in data from an ARFF file**.  You could create this matrix yourself within matlab and things would work just as well.  This is not a special data structure, just a double matrix.  This is the whole point of WEKALab:  It works fine with ordinary Matlab matrices.

A simple test to do here is 10 fold cross-validation using, say, decision trees:

```
>> [a c p m] = crossvalidate(M, 'tree', 10)
```

The output variables are, in order, the overall accuracy on the set, an n x n "confusion matrix" where n is the number of classes, an n x r matrix of predictions on each point where r is the number of points, and the model learned on the entire dataset.

The point-wise prediction vector, `p`, is something not available in the GUI that comes with WEKA, and proves very handy if you want a breakdown of exactly which points you've gotten wrong.  Each row in `p` has n columns, and each column is the "probability" of that point in the input matrix belonging to the corresponding class according to the model, so the first column in the `j`th row of `p` is the "probability" that the point `M(j,:)` belongs to class `0`, the second column to class `1`, etc.

Also note that the function does not randomize your data!  If you wish the data to be randomized, you must do it yourself beforehand!

To train a model on your dataset without testing it, do:

```
>> C = trainclassifier(M, 'tree');
```

If you wish to classify a point, define the point with the same number of colums as the input data and then use `getclassdistribution`:

```
>> k = [0 0 0 0 0];
>> getclassdistribution(C, k)
```

Note that `getclassdistribution` also works for matrices:

```
>> getclassdistribution(C, M)
```

The important thing, again, is that the matrix have the same number of columns as the training data.  The values you use for the classes, obviously, can be dummy values, but they must be there.

Finally, if you want to output your matrix to ARFF format for use in e.g., the WEKA GUI:

```
>> writearfffile(M, 'iris.test')
```

Note that the text class labels are not recovered (they are still integers) and there is a line at the very beginning of the file `%@SIZE`...  This is for some of my own C code that reads this data into a data structure and needs to preallocate.  WEKA itself ignores lines beginning with `%`, and so this file should read fine into WEKA.

## Extensions

To mess with the classifier options within WEKA, pass in an options string at training time.  For more information on how this works use:

```
>> help listoptions
```

Or, for example:

```
>> listoptions('svm_rbf')
```

Some classifiers, like `svm_rbf`, have their default options overridden by WEKALab (in this case, to select the RBF kernel).

For `svm_rbf` and `boost_tree`, you can pass in a second "internal options" string, that modifies the parameters of the weak classifier in the case of adaboost, and the parameters of the kernel in the case of `svm_rbf`.  You can again see these by using `listoptions`.

## Modification

To extend WekaLab to support more classifiers, or different sets of default options (e.g., a different default selection for 'k' in k-nearest neighbor), you need to modify `WekaInterface.java`, and recompile with `lein uberjar`.  This modification should be pretty straightforward, though, if you have decent access to the documentation.

One obvious current weakness is the lack of support for nominal attributes and numeric classes.  If someone wants to take this on, I'd love to review a pull request that adds support for it.
