package TP6.src.main.java;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerSelect {
    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(Integer.parseInt(args[0])));
            serverSocketChannel.configureBlocking(false); /** Enleve l'aspect bloquant */
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            int clientCount = 0;

            for(;;) {
                selector.select();
                Set<SelectionKey> selectkeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectkeys.iterator();
                ByteBuffer buffer = ByteBuffer.allocate(256);

                while(iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    clientCount++;

                    if(key.isAcceptable()) {
                        System.out.println("Client "+ clientCount+ " se connecte");
                        SocketChannel client = serverSocketChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    }
                    if(key.isReadable()) {
                        SocketChannel client = (SocketChannel)key.channel();
                        client.read(buffer);
                        String messageRecv = new String(buffer.array());
                        System.out.println("Client"+clientCount+" > "+ messageRecv.trim());

                        ByteBuffer messageSend = ByteBuffer.allocate(256);
                        String msg = "Connexion etablie\n";
                        messageSend.put(msg.getBytes());
                        client.write(messageSend);
                        messageSend.clear();
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
