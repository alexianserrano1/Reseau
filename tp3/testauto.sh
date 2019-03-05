#!/bin/bash

##### 
 
if (($#<3))
then 
	echo Mauvais nombre argument
	echo 3 arguments: le serveur java, netcat et le client java, C suivi du protocole
	echo Usage : ./testauto serveur client protocole
	exit 1
fi

if [ "$1" = "netcat" ]
then
	if [ "$3" = "udp" ]
	then
		echo "*************************************************"
		echo Ouverture netcat server UDP
		echo "*************************************************"
		gnome-terminal -x nc -u -l 1234
	elif [ "$3" = "tcp" ]
	then
		echo "*************************************************"
		echo Ouverture netcat server TCP
		echo "*************************************************"
		gnome-terminal -x nc -l 1234
	fi
	if [ "$2" = "c" ]
	then
		if [ "$3" = "udp" ]
		then
			gcc ClientUDP.c -o ClientUDP
			echo "*************************************************"
			echo ClientUDP C donnez le message a transmettre
			echo "*************************************************"
			./ClientUDP localhost 1234
		elif [ "$3" = "tcp" ]
		then
			gcc ClientTCP.c -o ClientTCP
			echo "*************************************************"
			echo ClientTCP C donnez le message a transmettre
			echo "*************************************************"
			./ClientTCP localhost 1234
		fi
	elif [ "$2" = "java" ]
	then
		if [ "$3" = "udp" ]
		then
			javac ClientUDP.java
			echo "*************************************************"
			echo ClientUDP Java donnez le message a transmettre
			echo "*************************************************"
			java ClientUDP localhost 1234
		elif [ "$3" = "tcp" ]
		then 
			javac ClientTCP.java
			echo "*************************************************"
			echo ClientTCP Java donnez le message a transmettre
			echo "*************************************************"
			java ClientTCP localhost 1234
		fi
	fi
fi

if [ "$1" = "java" ]
then
	if [ "$3" = "udp" ]
	then 
		javac ServerUDP.java
		echo "*************************************************"
		echo Ouverture Java server UDP
		echo "*************************************************"
		gnome-terminal -x java ServerUDP 1234
	elif [ "$3" = "tcp" ]
	then 
		javac ServerTCP.java
		echo "*************************************************"
		echo Ouverture Java server TCP
		echo "*************************************************"
		gnome-terminal -x java ServerTCP 1234
	fi
	
	if [ "$2" = "c" ]
	then
		if [ "$3" = "udp" ]
		then
			gcc ClientUDP.c -o ClientUDP
			echo "*************************************************"
			echo ClientUDP C donnez le message a transmettre
			echo "*************************************************"
			./ClientUDP localhost 1234
		elif [ "$3" = "tcp" ]
		then
			gcc ClientTCP.c -o ClientTCP
			echo "*************************************************"
			echo ClientTCP C donnez le message a transmettre
			echo "*************************************************"
			./ClientTCP localhost 1234
		fi
	elif [ "$2" = "java" ]
	then
		if [ "$3" = "udp" ]
		then
			javac ClientUDP.java
			echo "*************************************************"
			echo ClientUDP Java donnez le message a transmettre
			echo "*************************************************"
			java ClientUDP localhost 1234
		elif [ "$3" = "tcp" ]
		then
			javac ClientTCP.java
			echo "*************************************************"
			echo ClientTCP Java donnez le message a transmettre
			echo "*************************************************"
			java ClientTCP localhost 1234
		fi
	fi
fi


