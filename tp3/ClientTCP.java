import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientTCP {
    public static void main(String[] args){
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

            while(inFromUser.hasNextLine()) {
                String message=inFromUser.nextLine();
                outToServer.write(message+"\n");
                outToServer.flush();
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
