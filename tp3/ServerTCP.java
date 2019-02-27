import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class ServerTCP {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            Socket clientSocket = serverSocket.accept();

                while(true) {
                    BufferedReader inFromClient = new BufferedReader(
                            new InputStreamReader(
                                    clientSocket.getInputStream()));
                    String message = inFromClient.readLine();

                    if(message == null)
                        break;

                    System.out.println("> " + message);
                }
                clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
