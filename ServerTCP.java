import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;

public class ServerTCP {
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv6Addresses" , "true");
        try {
            if(args.length < 1)
                throw new Exception("Pas assez d'argument : necessite un port");

            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            int clientCount = 0;

            while(true) {
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                System.out.println("Connexion d'un client " + clientCount);
                Thread thread = new Thread(new ClientHandler(clientSocket, clientCount));
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    static class ClientHandler implements Runnable{
        Socket clientSocket;
        int numClient;

        public ClientHandler(Socket clientSocket, int numClient) {
            this.clientSocket = clientSocket;
            this.numClient = numClient;
        }

        public void run() {
            try {
                BufferedReader inFromClient = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter outToClient = new PrintWriter(
                        new OutputStreamWriter(
                                clientSocket.getOutputStream()));
                while(true) {
                    String message = inFromClient.readLine();
                    if (message == null) {
                        System.out.println("Deconnexion client " + numClient);
                        clientSocket.close();
						break;                    
					}
                    System.out.println("Client " + numClient+ " > " + message + "" + numClient);
                    outToClient.write("Connexion établie \n");
                    outToClient.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
