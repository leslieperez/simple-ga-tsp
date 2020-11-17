#!/bin/bash

mkdir -p bin 

javac -d bin/ -sourcepath src/ -cp lib/commons-cli-1.2.jar src/algorithms/*java

java -cp bin/:lib/commons-cli-1.2.jar algorithms.Runner
