import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Server {
    ArrayList<ConnectionProfil> profiles = new ArrayList<>();
    ByteBuffer readBuffer = ByteBuffer.allocate(256);
    ByteBuffer writeBuffer = ByteBuffer.allocate(256);

    private String getMessage(ByteBuffer buffer) {
        String msg = new String(buffer.array());
        int index = msg.indexOf("\n");
        return msg.substring(0, index);
    }

    private void broadcastExcept(ConnectionProfil profil, String msg) {
        for(int index = 0; index < profiles.size(); index++) {
            if(profiles.get(index).equals(profil)) {
                continue;
            }
            profiles.get(index).addMsgToQueue(msg);
        }
    }

    private void createProfile(SocketChannel sc, Selector s) throws IOException {
        ConnectionProfil profile = new ConnectionProfil(sc);
        profile.getSocket().register(s, SelectionKey.OP_READ, profile);
        profile.setKey(profile.getSocket().keyFor(s));
        profiles.add(profile);
    }

    private void acceptKey(ServerSocketChannel ssc, Selector s) throws IOException {
        SocketChannel client = ssc.accept();
        client.configureBlocking(false);
        createProfile(client, s);
    }

    private boolean bufferIsEmpty(ConnectionProfil p) throws IOException {
        return (p.getSocket().read(readBuffer) < 0);
    }

    private void readMessage(ConnectionProfil profile, Selector s) throws IOException {
        String messageRecv = getMessage(readBuffer);
        if (!messageRecv.equals("")) {
            String pseudo;
            if (messageRecv.startsWith("CONNECT")) {
                if (profile.isConnected) {
                    profile.addMsgToQueue("SERVER: ERROR CONNECT aborting clavardamu protocol.\n");
                }
                else {
                    pseudo = messageRecv.substring(8).trim();
                    profile.setPseudo(pseudo);
                    profile.setConnected();
                    profile.getSocket().register(s, SelectionKey.OP_READ | SelectionKey.OP_WRITE, profile);
                    broadcastExcept(profile, "SERVER: Connexion de " + pseudo + "\n");
                    System.out.println("Connexion "+pseudo);
                }
            }
            else if(messageRecv.startsWith("MSG")){
                if(profile.isConnected) {
                    pseudo = profile.getPseudo();
                    broadcastExcept(profile, pseudo + "> " + messageRecv.substring(4)+"\n");
                }
                else {
                    profile.addMsgToQueue("SERVER: ERROR clavardamu.\n");
                }
            }
            else { profile.addMsgToQueue("SERVER: ERROR clavardamu.\n"); }
        }
        readBuffer.flip();
    }

    private void readKey(SelectionKey key, Selector s) throws IOException {
        ConnectionProfil profile = (ConnectionProfil) key.attachment();

        if(bufferIsEmpty(profile)) {
            profile.isConnected = false;
            System.out.println("Deconnexion "+profile.getPseudo());
            profiles.remove(profile);
            profile.getSocket().close();
            return;
        }
        else { readMessage(profile, s); }
        readBuffer.clear();
    }

    private void writeKey(SelectionKey key, Selector s) throws IOException {
        ConnectionProfil profil = (ConnectionProfil) key.attachment();
        String msg = profil.popMessage();
        if (msg != null) {
            writeBuffer.clear();
            writeBuffer.put(msg.getBytes());
            writeBuffer.flip();
            profil.getSocket().write(writeBuffer);
        }
        if(profil.queue.isEmpty())
            key.interestOps(SelectionKey.OP_READ);
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

                    if(key.isValid() && key.isAcceptable()) { acceptKey(serverSocketChannel, selector); }
                    if(key.isValid() && key.isReadable()) { readKey(key, selector); }
                    if(key.isValid() && key.isWritable()) { writeKey(key, selector); }

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
