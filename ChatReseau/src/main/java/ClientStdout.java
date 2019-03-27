import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Semaphore;

public class ClientStdout implements Runnable {
    private Socket socket;
    private Semaphore semaphore;

    public ClientStdout(Socket socket, Semaphore semaphore) {
        this.socket = socket;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            BufferedReader inFromServer = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            for(;;) {
                semaphore.acquire();
                if(socket.isClosed()) {
                    semaphore.release();
                    break;
                }
                semaphore.release();

                String message = inFromServer.readLine();

                if(message == null) {
                    System.out.println("Server> Fin de connexion");
                    break;
                }
                System.out.println("Server> "+ message.trim());
            }

            semaphore.acquire();
            if(!socket.isClosed()) {
                socket.close();
                System.exit(0);
            }
            semaphore.release();

        } catch (SocketException se) {
            System.out.println("Fermeture du stdout");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("stdout");
            System.exit(1);
        }

    }
}
