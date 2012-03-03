function listoptions(type)
    %LISTOPTIONS lists the options available for a given classifier type
    %   LISTOPTIONS shows the options available for a given classifier type
    %   (of the form shown by listclassifiers).  This function will give a
    %   number of switches (of the form -X) which may have one or more
    %   arguments that follow them.  To set these switches during learning,
    %   compose a string of them such as '-X 20 -Y -Z 4 5' and pass them in
    %   to the opt parameter of TRAINCLASSIFIER, TRAINANDTEST, or
    %   CROSSVALIDATE.
    org.clparker.wekalab.WekaInterface.listOptions(getmodel(type));
end