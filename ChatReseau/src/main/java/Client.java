import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            Semaphore semaphore = new Semaphore(1);
            new Thread(new ClientStdin(socket, semaphore)).start();
            new Thread(new ClientStdout(socket, semaphore)).start();


        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
