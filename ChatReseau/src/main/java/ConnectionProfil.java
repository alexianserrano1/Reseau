import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionProfil {
    SocketChannel sc;
    String pseudo;
    ArrayBlockingQueue<String> queue;

    public ConnectionProfil(SocketChannel sc) {
        this.sc = sc;
        queue = new ArrayBlockingQueue<>(500);
    }

    void setPseudo(String pseudo) { this.pseudo = pseudo; }
    void addMsgToQueue(String message) { this.queue.add(message); }
    String popMessage() { return this.queue.poll(); }
    String getPseudo() { return pseudo; }
    SocketChannel getSocket() { return this.sc; }
}
