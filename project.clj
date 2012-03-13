(defproject org.clparker/wekalab "0.3.0-SNAPSHOT"
  :description "Extremely simple interface from WEKA to MATLAB"
  :java-source-path "src/java"
  :javac-options {:source "1.6" :target "1.6" :bootclasspath "jdk1.6.0/lib/rt.jar"}
  :uberjar-name "wekalab.jar"
  :dependencies [[weka "3.6.2"]])
