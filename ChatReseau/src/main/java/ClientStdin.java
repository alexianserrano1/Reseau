import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class ClientStdin implements Runnable {
    private Socket socket;
    private Semaphore semaphore;

    public ClientStdin(Socket socket, Semaphore semaphore) {
        this.socket = socket;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            PrintWriter outToServer = new PrintWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream()));
            Scanner inFromUser = new Scanner(System.in);

            System.out.print("Entrez votre pseudo: ");
            String pseudo = inFromUser.next();
            outToServer.write("CONNECT "+ pseudo +"\n");
            outToServer.flush();

            while(true) {
                semaphore.acquire();
                if(socket.isClosed()) {
                    semaphore.release();
                    break;
                }
                semaphore.release();
                System.out.println("aaaaaaaaaaaaa");
                if(inFromUser.hasNextLine()) {
                    String message = inFromUser.nextLine();

                    outToServer.write(message + "\n");
                    outToServer.flush();
                }
            }
            System.out.println("aaaaaaaaaaaaa");
            semaphore.acquire();
            System.out.println("bbbbbbbbbbbb");
            if(!socket.isClosed())
                socket.close();
            semaphore.release();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
