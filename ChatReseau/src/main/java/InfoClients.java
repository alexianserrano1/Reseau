import java.util.HashMap;
import java.util.Queue;

public class InfoClients {
    public String[] pseudos = new String[500];
    int size = 0;
    Queue<String>[] messToSend = new Queue[500];

    public void addPseudo (String pseudo) {
        pseudos[getSize()] = pseudo;
        size++;
    }

    public int getSize() { return size; }

    public void addMessage(int numClient, String message) {
        messToSend[numClient].add(message);
    }

    public String getMessage (int numClient) { /** Peut renvoyer null si aucun message à envoyer */
        return messToSend[numClient].poll();
    }
}
