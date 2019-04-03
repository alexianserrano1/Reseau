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
                if(message.startsWith("SERVER:"))
                    System.out.println("Server> "+ message.substring(7).trim());
                else
                    System.out.println(message.trim());
            }

            semaphore.acquire();
            if(!socket.isClosed()) {
                socket.close();
                System.exit(0);
            }
            semaphore.release();

        } catch (SocketException se) {
            System.out.println("Vous quittez le seveur");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
