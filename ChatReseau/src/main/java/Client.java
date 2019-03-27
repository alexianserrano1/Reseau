import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            Semaphore semaphore = new Semaphore(1);
            Thread t1 = new Thread(new ClientStdin(socket, semaphore));
            t1.start();
            Thread t2 = new Thread(new ClientStdout(socket, semaphore));
            t2.start();

            t1.join();
            t2.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("client");
            System.exit(1);
        }
    }

}
