#/bin/bash
HOMEDIR=/opt/timesys/rtsj-ri
BIN=../bin
export LD_ASSUME_KERNEL=2.4.1; 

$HOMEDIR/bin/tjvm -Djava.class.path=$BIN -Xbootclasspath=$HOMEDIR/lib/foundation.jar rtsjcomponents.example2.Example2
