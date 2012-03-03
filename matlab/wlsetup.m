function wlsetup()
    %WLSETUP sets up the java path to work with WekaLab
    %   WLSETUP adds two entries to classpath.txt.  One is for the 
    %   jar file for the java interface files that connect matlab with 
    %   WEKA.  The other is the jar file containing the WEKA classes.  MATLAB
    %   should be restarted after executing this command for the classpath 
    %   to be reloaded.
    
    s = which('wlsetup');
    wdir = s(1:end - 10);
    wjar = [wdir '\weka.jar'];
    wljar = [wdir '\WEKALab.jar'];
    clspth = [matlabroot '\toolbox\local\classpath.txt'];
    fout = fopen(clspth, 'a');
    fprintf(fout, '%s\n', wjar);
    fprintf(fout, '%s\n', wljar);
    addpath(wdir);
    fclose(fout);
    fprintf('wlsetup completed!\n');
end