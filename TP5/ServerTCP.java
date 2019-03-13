import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;

public class ServerTCP {
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv6Addresses" , "true");
        try {
            if(args.length < 1)
                throw new Exception("Pas assez d'argument : necessite un port");

            String arg1 = args[0];

            int clientCount = 0;
            ServerSocket serverSocket; /*= new ServerSocket(Integer.parseInt(args[0]));*/

            switch (arg1) {
                case "-t":
                    serverSocket = new ServerSocket(Integer.parseInt(args[2]));
                    while(true) {
                        clientCount ++;
                        long startTime = System.nanoTime();
                        Executors.newFixedThreadPool(Integer.parseInt(args[1])).execute(
                                new ClientHandler(serverSocket.accept(), clientCount));
                        long estimatedTime = System.nanoTime() - startTime;
                    }
                case "-s":
                    serverSocket = new ServerSocket(Integer.parseInt(args[1]));
                    while(true) {
                        clientCount ++;
                        long startTime = System.nanoTime();
                        Executors.newWorkStealingPool().execute(
                                new ClientHandler(serverSocket.accept(), clientCount));
                        long estimatedTime = System.nanoTime() - startTime;
                    }
                    default:
                        serverSocket = new ServerSocket(Integer.parseInt(args[0]));
                        while(true) {
                            clientCount ++;
                            long startTime = System.nanoTime();
                            Executors.newCachedThreadPool().execute(
                                    new ClientHandler(serverSocket.accept(), clientCount));
                            long estimatedTime = System.nanoTime() - startTime;
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        /** ancienne version */
            /*while(true) {
                clientCount ++;
                long startTime = System.nanoTime();
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new ClientHandler(serverSocket.accept(), clientCount));
                long estimatedTime = System.nanoTime() - startTime;
             }*/


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
                    System.out.println("Client " + numClient+ " > " + message);
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
