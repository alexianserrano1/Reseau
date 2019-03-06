import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientTCP {
    public static void main(String[] args){
        System.setProperty("java.net.preferIPv6Addresses" , "true");
        try {
            if(args.length < 2)
                throw new Exception("Pas assez d'arguments");

            Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
            BufferedReader inFromServer = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            PrintWriter outToServer = new PrintWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream()));
            Scanner inFromUser = new Scanner(System.in);

            String message= "Connexion client ";
            outToServer.write(message+"\n");
            outToServer.flush();

            String reçu = inFromServer.readLine();

            outToServer.write("Deconnexion");
            outToServer.flush();

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}