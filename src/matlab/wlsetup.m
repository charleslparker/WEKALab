function wlsetup()
    %WLSETUP sets up the java path to work with WekaLab
    %   WLSETUP adds an entry to the java class path at classpath.txt
    %   for the jar containing WEKALab.  Note that the WEKALab jar includes
    %   the WEKA jar, so there is no need for an additional entry.
    
    s = which('wlsetup');
    wdir = s(1:end - 21);
    wljar = [wdir '\wekalab.jar'];

    if isempty(ls(wljar))
        error(['wekalab.jar not found in ' wdir])
    end
    
    clspth = [matlabroot '\toolbox\local\classpath.txt'];
    fout = fopen(clspth, 'a');
    fprintf(fout, '%s\n', wljar);
    addpath(wdir);
    fclose(fout);
    fprintf('wlsetup completed!\n');
end