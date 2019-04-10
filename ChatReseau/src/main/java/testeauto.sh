#!/bin/bash

#####


if (($#<1))
then 
	echo mauvais nombre arg
	echo usage : ./autotest nombre_clients
fi

javac Server.java
echo Server
gnome-terminal -x java Server

javac Client.java

for ((i=0; i<"$1"; i++))
do 
	gnome-terminal -x java Client 
done

##-e 'sh -c "echo CONNECT $$ | java Client ; exec bash"'
