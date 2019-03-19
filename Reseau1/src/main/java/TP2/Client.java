package TP2;

import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        try {
            if(args.length < 2)
                throw new Exception("Pas assez d'arguments");

            DatagramSocket socket = new DatagramSocket();
            byte[] message = new byte[25];

            Scanner scanner = new Scanner(System.in);
            String msg;
            InetAddress localhost = InetAddress.getByName(args[0]);

            while(scanner.hasNextLine()) {
                msg = scanner.nextLine() + "\n";
                message = msg.getBytes();
                DatagramPacket out = new DatagramPacket(
                        message, message.length, localhost, Integer.parseInt(args[1]));
                socket.send(out);


				/** Cas où le serveur renvoi un message*/
                /*byte[] inBytes = new  byte[25];
                DatagramPacket inPacket = new DatagramPacket(inBytes, inBytes.length);
                socket.receive(inPacket);
                System.out.println(new String(inPacket.getData(), 0, inPacket.getLength()));*/
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
