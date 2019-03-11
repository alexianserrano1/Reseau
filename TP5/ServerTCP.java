import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.concurrent.*;
public class ServerTCP {
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv6Addresses", "true");


        int clientCount = 0;


        if (args[0] == "EchoServerDPool") {

            try {
                ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[1]));
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                Executor e = Executors.newCachedThreadPool();
                e.execute(new ClientHandler(clientSocket, clientCount));

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        else if (args[0] == "EchoServerSPool") {
            try {
                ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[2]));
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                Executor e = Executors.newFixedThreadPool(Integer.parseInt(args[1]));
                e.execute(new ClientHandler(clientSocket, clientCount));

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }


        }

        else {
            try {
                while (true) {
                    ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
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
