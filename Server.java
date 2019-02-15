import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(1234);
            byte[] message = new byte[25];
            DatagramPacket in = new DatagramPacket(message, 25);

            while(true) {
                socket.receive(in);
                String left = new String(
                        in.getData(), in.getOffset(), in.getLength());
                message = ("> " + left).getBytes();
                DatagramPacket out = new DatagramPacket(
                        message, 0, message.length, in.getAddress(), in.getPort());
                socket.send(out);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
