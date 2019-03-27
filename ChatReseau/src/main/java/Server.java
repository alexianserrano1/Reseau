import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Server {
    static ConnectionProfil[] profiles = new ConnectionProfil[500];

    private static String getMessage(ByteBuffer buffer) {
        String msg = new String(buffer.array());
        int index = msg.indexOf("\n");
        return msg.substring(0, index);
    }

    private static void sendAllExcept(ConnectionProfil profil, String msg) {
        for(int index = 0; index < profiles.length; index++) {
            if(profiles[index].equals(profil))
                continue;
            profiles[index].addMsgToQueue(msg);
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(12345));
            serverSocketChannel.configureBlocking(false); /** Enleve l'aspect bloquant */
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(256);


            int clientCount = 0;

            for(;;) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while(iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if(key.isAcceptable()) {
                        SocketChannel client = serverSocketChannel.accept();
                        client.configureBlocking(false);
                        ConnectionProfil profil = new ConnectionProfil(client);
                        client.register(selector, SelectionKey.OP_READ, profil);
                        profiles[clientCount] = profil;
                        clientCount++;
                    }
                    if(key.isReadable()) {
                        SocketChannel client = (SocketChannel)key.channel();
                        String pseudo;
                        if(client.read(buffer) < 0) {
                            key.cancel();
                            buffer.clear();
                            iterator.remove();
                            continue;
                        }
                        String messageRecv = Server.getMessage(buffer);
                        if(!messageRecv.equals("")) {
                            if(messageRecv.startsWith("CONNECT")) {
                                ConnectionProfil profil = (ConnectionProfil)key.attachment();
                                pseudo = messageRecv.substring(8).trim();
                                profil.setPseudo(pseudo);
                                key.attach(profil);
                                Server.sendAllExcept(profil, "Connexion de "+pseudo);
                                client.register(selector, SelectionKey.OP_WRITE);
                            }
                            else if(messageRecv.startsWith("MSG")) {
                                ConnectionProfil profil = (ConnectionProfil)key.attachment();
                                pseudo = profil.getPseudo();
                                Server.sendAllExcept(profil, pseudo+"> "+ messageRecv.substring(4));
                            }
                        }
                        buffer.clear();
                    }
                    if(key.isWritable()) {
                        SocketChannel client = (SocketChannel)key.channel();
                        ConnectionProfil profil = (ConnectionProfil)key.attachment();
                        String msg = profil.pop();
                        if(msg != null) {
                            buffer.put(msg.getBytes());
                            client.write(buffer);
                        }
                        buffer.clear();
                    }
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
