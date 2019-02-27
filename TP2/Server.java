import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(1234);
            byte[] messagebyte = new byte[25];
            DatagramPacket in = new DatagramPacket(messagebyte, 25);

            while (true) {
                socket.receive(in);
                String left = new String(
                        in.getData(), in.getOffset(), in.getLength());

                String message = "> " + left;
                DatagramPacket out = new DatagramPacket(
                        message.getBytes(), message.getBytes().length, in.getAddress(), in.getPort());
                socket.send(out);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
