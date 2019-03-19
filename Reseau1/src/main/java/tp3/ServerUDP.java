package tp3;

import java.net.*;

public class ServerUDP {
    public static void main(String[] args) {
        try {
        	if(args.length < 1) {
        		throw new Exception("Not enough args : need a port");
        	}
        
            DatagramSocket socket = new DatagramSocket(Integer.parseInt(args[0]));
            byte[] messagebyte = new byte[25];
            DatagramPacket in = new DatagramPacket(messagebyte, 25);

            while (true) {
                socket.receive(in);
                String message = new String(
                        in.getData(), in.getOffset(), in.getLength());

                System.out.print("> "+ message);
			}

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
