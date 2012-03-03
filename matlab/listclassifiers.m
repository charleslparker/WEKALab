function listclassifiers()
    %LISTCLASSFIERS lists the classifiers available in WekaLab
    %   LISTCLASSIFIERS shows the defined constants available for use as
    %   the 'type' paramater in TRAINANDTEST, CROSSVALIDATE, and
    %   TRAINCLASSIFIER.  These are string constants.
    org.clparker.wekalab.WekaInterface.listClassifiers();
end