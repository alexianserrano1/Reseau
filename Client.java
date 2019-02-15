import java.io.IOException;
import java.net.*;
import java.lang.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            if(args.length < 2)
                throw new Exception("Pas assez d'arguments");

            DatagramSocket socket = new DatagramSocket();
            byte[] message;

            Scanner in = new Scanner(System.in);
            String msg;
            InetAddress localhost = InetAddress.getByName(args[0]);

            while(in.hasNextLine()) {
                msg = in.nextLine();
                message = msg.getBytes();
                DatagramPacket out = new DatagramPacket(
                        message, message.length, localhost, Integer.parseInt(args[1]));
                socket.send(out);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
