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
	echo "*************************************************"
	echo Ouverture netcat server
	echo "*************************************************"
	gnome-terminal -x nc -u -l 1234
	if [ "$2" = "c" ]
	then
	gcc ClientUDP.c -o ClientUDP
	echo "*************************************************"
	echo ClientUDP C donnez le message a transmettre
	echo "*************************************************"
	./$EXEC localhost 1234
	elif [ "$2" = "java" ]
	then
	javac Client.java
	echo "*************************************************"
	echo ClientUDP Java donnez le message a transmettre
	echo "*************************************************"
	java Client localhost 1234
	fi
fi

if [ "$1" = "java" ]
	then
	javac Server.java
	echo "*************************************************"
	echo Ouverture Java server
	echo "*************************************************"
	gnome-terminal -x java Server 1234
	if [ "$2" = "c" ]
	then
	gcc ClientUDP.c -o ClientUDP
	echo "*************************************************"
	echo ClientUDP C donnez le message a transmettre
	echo "*************************************************"
	./$EXEC localhost 1234
	elif [ "$2" = "java" ]
	then
	javac Client.java
	echo "*************************************************"
	echo ClientUDP Java donnez le message a transmettre
	echo "*************************************************"
	java Client localhost 1234
	fi
fi


