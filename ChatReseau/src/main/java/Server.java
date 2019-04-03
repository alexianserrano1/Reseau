import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Server {
    ArrayList<ConnectionProfil> profiles = new ArrayList<>();
    ByteBuffer buffer = ByteBuffer.allocate(256);

    private String getMessage(ByteBuffer buffer) {
        String msg = new String(buffer.array());
        int index = msg.indexOf("\n");
        return msg.substring(0, index);
    }

    private void broadcast(ConnectionProfil profil, String msg) {
        for(int index = 0; index < profiles.size(); index++) {
            if(profiles.get(index).equals(profil))
                continue;
            profiles.get(index).addMsgToQueue(msg);
        }
    }

    private void createProfile(SocketChannel sc, Selector s) throws IOException {
        ConnectionProfil profile = new ConnectionProfil(sc);
        profile.getSocket().register(s, SelectionKey.OP_READ, profile);
        profiles.add(profile);
    }

    private void acceptKey(ServerSocketChannel ssc, Selector s) throws IOException {
        SocketChannel client = ssc.accept();
        client.configureBlocking(false);
        createProfile(client, s);
    }

    private boolean bufferIsEmpty(ConnectionProfil p) throws IOException {
        return (p.getSocket().read(buffer) < 0);
    }

    private void cancelKey(SelectionKey key) {
        key.cancel();
        buffer.clear();
    }

    private boolean isAlreadyConnected(ConnectionProfil profil) {
        for(ConnectionProfil p : profiles) {
            if(profil.equals(p)) return true;
        }
        return false;
    }

    private void readMessage(ConnectionProfil profile, Selector s) throws IOException {
        String messageRecv = getMessage(buffer);
        if (!messageRecv.equals("")) {
            String pseudo;
            if (messageRecv.startsWith("CONNECT")) {
                if (isAlreadyConnected(profile)) {

                } else {
                    pseudo = messageRecv.substring(8).trim();
                    profile.setPseudo(pseudo);
                    profile.getSocket().register(s, SelectionKey.OP_WRITE, profile);
                    broadcast(profile, "Connexion de " + pseudo);
                }
            }
            else if(messageRecv.startsWith("MSG")){
                pseudo = profile.getPseudo();
                broadcast(profile, pseudo + "> " + messageRecv.substring(4));
            }

        }
    }

    private void readKey(SelectionKey key, Selector s) throws IOException {
        ConnectionProfil profile = (ConnectionProfil) key.attachment();

        if(bufferIsEmpty(profile)) {
            cancelKey(key);
            return;
        }
        else { readMessage(profile, s); }
        buffer.clear();
    }

    private void writeKey(SelectionKey key, Selector s) throws IOException {
        ConnectionProfil profil = (ConnectionProfil) key.attachment();
        String msg = profil.popMessage();
        System.out.println(msg);
        if (msg != null) {
            System.out.println(msg);
            buffer.put(msg.getBytes());
            profil.getSocket().write(buffer);
        }
        buffer.clear();
        profil.getSocket().register(s, SelectionKey.OP_READ, profil);
    }

    public void start() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(12345));
            serverSocketChannel.configureBlocking(false); /** Enleve l'aspect bloquant */
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            for(;;) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while(iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if(key.isAcceptable()) { acceptKey(serverSocketChannel, selector); }
                    if(key.isReadable()) { readKey(key, selector); }
                    if(key.isWritable()) { writeKey(key, selector); }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
