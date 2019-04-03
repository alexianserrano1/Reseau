import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionProfil {
    SocketChannel sc;
    String pseudo;
    ArrayBlockingQueue<String> queue;
    boolean isConnected = false;
    SelectionKey key = null;

    public ConnectionProfil(SocketChannel sc) {
        this.sc = sc;
        queue = new ArrayBlockingQueue<>(500);
    }

    void setPseudo(String pseudo) { this.pseudo = pseudo; }

    void setKey(SelectionKey key) { this.key = key; }

    void addMsgToQueue(String message) {
        if(queue.isEmpty())
            key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);

        queue.add(message);
    }

    String popMessage() { return this.queue.poll(); }
    String getPseudo() { return pseudo; }
    SocketChannel getSocket() { return this.sc; }
    void setConnected() { isConnected = true; }
}
