#!/bin/bash

##### 
 
EXEC=ClientUDP




if (($#<2))
then 
	echo mauvais nombre arg
	echo usage : ./autotest typeserv java ou netcat typeclient c ou java
fi





if [ "$1" = "netcat" ]
	then
	echo ouverture netcat server
	gnome-terminal -x nc -u -l 1234
	if [ "$2" = "c" ]
	then
	gcc ClientUDP.c -o ClientUDP
	echo client c donnez le message a transmettre
	./$EXEC localhost 1234
	elif [ "$2" = "java" ]
	then
	javac Client.java
	echo client java donnez le message a transmettre
	java Client localhost 1234
	fi
fi

if [ "$1" = "java" ]
	then
	javac Server.java
	echo ouverture java server 
	gnome-terminal -x java Server 1234
	if [ "$2" = "c" ]
	then
	gcc ClientUDP.c -o ClientUDP
	echo client c donnez le message a transmettre
	./$EXEC localhost 1234
	elif [ "$2" = "java" ]
	then
	javac Client.java
	echo client java donnez le message a transmettre
	java Client localhost 1234
	fi
fi


	

