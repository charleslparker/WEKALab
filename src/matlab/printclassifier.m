function printclassifier(model)
    %PRINTCLASSIFIER prints classifier information
    %   PRINTCLASSIFIER shows information in string form about a classifier
    %   learned by TRAINCLASSIFIER.  This can be the structure for a tree,
    %   or the support vectors of an SVM.
    model.printModel();
end