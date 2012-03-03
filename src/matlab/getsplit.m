function [train test sind eind] = getsplit(in, fold, numfolds)
    sind = round(rows(in)*(fold - 1)/numfolds) + 1;
    eind = round(rows(in)*fold/numfolds);
    train = [in(1:sind-1,:); in(eind+1:end,:)];
    test = in(sind:eind,:);
end