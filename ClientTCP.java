import java.io.*;
import java.net.*;

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
            BufferedReader inFromUser = new BufferedReader(
                    new InputStreamReader(
                            System.in));

            String message;
            while((message = inFromUser.readLine()) != null) {
                outToServer.write(message);
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
