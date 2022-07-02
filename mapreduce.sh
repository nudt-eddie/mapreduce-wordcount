#java version "1.8.0_331"
#Java(TM) SE Runtime Environment (build 1.8.0_331-b09)
#Java HotSpot(TM) 64-Bit Server VM (build 25.331-b09, mixed mode)

#!/bin/bash
if [ ! -d "./output" ]; then
  mkdir ./output
fi
echo compiling...
filename=$(basename "$1")
filename="${filename%.*}"
javac -Djava.ext.dirs=$HADOOPPATH $1 -d output
cd output
echo packing...
jar -cvf $filename.jar *.class
echo running...
/usr/local/hadoop/bin/hadoop fs -rm -r output/
/usr/local/hadoop/bin/hadoop jar $filename.jar $filename input output
cd ..

