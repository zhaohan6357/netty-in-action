package nia.chapter4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
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
public class PlainNioClient {

    private static final Logger logger = LoggerFactory.getLogger(PlainNioClient.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        PlainNioClient client = new PlainNioClient();
        client.connect(12346);
    }

    public void connect(int port) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel
                .open(/*new InetSocketAddress("127.0.0.1", 12346)*/);
        System.out.println(socketChannel.connect(new InetSocketAddress("127.0.0.1", 12346)));
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);//SelectionKey.OP_READ表示读就绪事件
        //启动读取线程

        //Thread.sleep(3000);
        System.out.println(socketChannel.isBlocking());
        while (true) {
            logger.info("selecting");
            Thread.sleep(1000);
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            logger.info("selected {} keys", selectionKeys.size());

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                logger.info("get key {}", key.readyOps());

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

                    ByteBuffer bufferToSend = ByteBuffer.wrap("i am client!\r\n".getBytes());
                    System.out.println(buffer.hasRemaining());
                    channel.write(bufferToSend);

                    logger.info("writing done");

                    //channel.close();
                }

            }
        }

    }
}
