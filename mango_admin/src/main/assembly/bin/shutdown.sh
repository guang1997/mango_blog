#! /bin/bash
#查找该端口的进程信息,并将第二列的进程号赋给ID
ID=`lsof -i:10001 |grep java |awk '{print $2}'`
echo $ID
kill -9 $ID
echo "kill success!"