function vec = makeclassvector(invec)
    %MAKECLASSVECTOR changes a class vector into WekaLab format
    %   MAKECLASSVECTOR takes a vector of integers and normalizes it such
    %   its elements begin with zero and end with n - 1, where there are n
    %   unique elements in the original vector.  The ordering of the
    %   elements is maintained.  For example:
    %   
    %   [20 5 5 9 20 3 9 9]
    %   
    %   will become
    %
    %   [3 1 1 2 3 0 2 2]
    
    univec = unique(invec);
    vec = invec;
    for i = 1:rows(univec);
        vec(find(vec == univec(i))) = i - 1;
    end
end