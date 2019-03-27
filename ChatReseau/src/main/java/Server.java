import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(12345));
            serverSocketChannel.configureBlocking(false); /** Enleve l'aspect bloquant */
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(256);

            InfoClients infoClients;
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
                        client.register(selector, SelectionKey.OP_READ);

                    }
                    if(key.isReadable()) {
                        SocketChannel client = (SocketChannel)key.channel();
                        client.read(buffer);
                        String messageRecv = new String(buffer.array());
                        key.attach(messageRecv.substring(8).trim());
                        System.out.println(messageRecv.substring(8).trim()+"> "+ messageRecv.trim());
                        clientCount++;
                        client.register(selector, SelectionKey.OP_WRITE);
                    }
                    if(key.isWritable()) {
                        SocketChannel client = (SocketChannel)key.channel();
                        String msg = "Connexion etablie\n";
                        buffer.put(msg.getBytes());
                        client.write(buffer);
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
