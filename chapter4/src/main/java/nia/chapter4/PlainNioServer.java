package nia.chapter4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listing 4.2 Asynchronous networking without Netty
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class PlainNioServer {

    private static final Logger logger = LoggerFactory.getLogger(PlainNioServer.class);

    public static void main(String[] args) throws IOException {
        PlainNioServer server = new PlainNioServer();

        server.serve(12346);
    }

    public void serve(int port) throws IOException {

        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        ServerSocket ss = serverChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        ss.bind(address);
        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println(serverChannel.isBlocking());
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
        for (;;) {
            try {
                System.out.println("selecting...");
                selector.select();
            } catch (IOException ex) {
                ex.printStackTrace();
                //handle exception
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            System.out.println(String.format("selected %s keys", readyKeys.size()));
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                logger.info("get key {}", key.readyOps());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                iterator.remove();
                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, /*SelectionKey.OP_WRITE |*/ SelectionKey.OP_READ,
                                msg.duplicate());
                        ByteBuffer buffer2 = ByteBuffer.wrap("i am server!\r\n".getBytes());
                        logger.info("write to client {}", buffer2.hasRemaining());
                        client.write(buffer2);
                        System.out.println("Accepted connection from " + client);
                    }
                    if (key.isReadable()) {
                        logger.info("key is readable");
                        ByteBuffer buffer = ByteBuffer.allocate(30);
                        buffer.clear();
                        SocketChannel channel = (SocketChannel) key.channel();
                        int readLen = channel.read(buffer);
                        if (readLen == -1) {
                            logger.info("read nothing");
                        } else {
                            buffer.flip();
                            String readStr = StandardCharsets.UTF_8.decode(buffer).toString();
                            logger.info("read str {}", readStr);
                        }
                        ByteBuffer buffer2 = ByteBuffer.wrap("i am server!\r\n".getBytes());
                        logger.info("write to client {}", buffer2.hasRemaining());
                        channel.write(buffer2);
                      //  channel.close();
                    }
                    /* if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        */
                    /*
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    *//*
                                    
                                    ByteBuffer buffer = ByteBuffer.wrap("i am server!\r\n".getBytes());
                                    
                                    logger.info("key is writable {}", buffer.hasRemaining());
                                    while (buffer.hasRemaining()) {
                                      if (client.write(buffer) == 0) {
                                          // break;
                                      }
                                    }
                                    
                                    client.close();
                                    }*/

                } catch (IOException ex) {
                    ex.printStackTrace();
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                        // ignore on close
                    }
                }
            }
        }
    }
}
