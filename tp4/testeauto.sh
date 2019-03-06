#!/bin/bash

#####


if (($#<1))
then 
	echo mauvais nombre arg
	echo usage : ./autotest protocole_ip
fi


if [ "$1" = "ipv4" ]
then
	javac ServerTCPSimpleStack.java
	echo "*************************************************"
	echo ServeurTCP IPv4 Java donnez le message a transmettre
	echo "*************************************************"
	gnome-terminal -x java ServerTCPSimpleStack 1234

	javac ClientTCPIPv4.java
	echo "*************************************************"
	echo ClientTCP IPv4 Java donnez le message a transmettre
	echo "*************************************************"
	java ClientTCPIPv4 localhost 1234
fi

if [ "$1" = "ipv6" ]
then
	javac ServerTCP.java
	echo "*************************************************"
	echo ServeurTCP double stack Java donnez le message a transmettre
	echo "*************************************************"
	gnome-terminal -x java ServerTCP 1234

	javac ClientTCP.java
	echo "*************************************************"
	echo ClientTCP IPv6 Java donnez le message a transmettre
	echo "*************************************************"
	java ClientTCP localhost 1234
fi
