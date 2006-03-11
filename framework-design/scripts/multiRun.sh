#!/bin/sh
HOMEDIR=/opt/timesys/rtsj-ri
BIN=../bin
export LD_ASSUME_KERNEL=2.4.1; 

DIR_BASENAME=TEST
MAX_TESTCASES=5
MAX_RUNS=10


if [ "$1" = "jrate" ]; then
  echo "SCRIPT: Running jRate!"
  CMD=../jrate-bin/run-me.exe
elif [ "$1" = "tjvm" ]; then
  echo "SCRIPT: Running tjvm!"
  CMD="$HOMEDIR/bin/tjvm -Djava.class.path=$BIN -Xbootclasspath=$HOMEDIR/lib/foundation.jar rtsjcomponents.example2.Example2"
elif [ "$1" = "clean" ]; then
  rm -Rf "$DIR_BASENAME"*
  exit
else
  echo "Usage: multiRun.sh [jrate|tjvm|clean]";
  exit  
fi

T=0
R=0

while [ "$T" -le "$MAX_TESTCASES" ] 
do
    while [ "$R" -lt "$MAX_RUNS" ] 
    do
       $CMD $T $R
#       echo $CMD $T $R
       R=`expr $R + 1`;
    done;
    R=0;    
    OUTPUT_DIR=./"$DIR_BASENAME"_"$T";
    mkdir $OUTPUT_DIR
    mv *.txt $OUTPUT_DIR/.
    T=`expr $T + 1`;
done
