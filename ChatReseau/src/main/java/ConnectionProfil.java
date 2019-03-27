import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionProfil {
    SocketChannel sc;
    String pseudo;
    ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(500);

    public ConnectionProfil(SocketChannel sc) {
        this.sc = sc;
    }

    void setPseudo(String pseudo) { this.pseudo = pseudo; }
    void addMsgToQueue(String message) { this.queue.add(message); }
    String pop() { return this.queue.poll(); }
    String getPseudo() { return pseudo; }
}
