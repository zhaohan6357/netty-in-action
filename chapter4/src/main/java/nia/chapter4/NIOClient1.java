package nia.chapter4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NIOClient1 {

    private Selector selector;
    private SocketChannel socketChannel;
    private SocketAddress address;

    public NIOClient1(String host, int port) throws IOException {
        address = new InetSocketAddress(host, port);
        selector = Selector.open();
        socketChannel = SocketChannel.open(address);
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 12346;
        try {
            NIOClient1 client = new NIOClient1(host, port);

            Selector selector = client.getSelector();
            while (true) {
                System.out.println("selecting");
                if (selector.select() > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int size = socketChannel.read(buffer);
                            if (size > 0) {
                                buffer.flip();
                                byte[] data = new byte[size];
                                buffer.get(data);
                                System.out.println("来自服务端的消息：\t" + new String(data));
                            }

                        }
                        iterator.remove();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Selector getSelector() {
        return selector;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void setAddress(SocketAddress address) {
        this.address = address;
    }
}
