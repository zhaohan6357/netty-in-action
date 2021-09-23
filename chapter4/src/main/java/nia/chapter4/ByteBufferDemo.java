package nia.chapter4;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author zhaohan
 * Created on 2021-09-23
 */
public class ByteBufferDemo {

    public static void main(String[] args) throws IOException {

        FileInputStream fis = new FileInputStream(new File("/tmp/t"));
        FileChannel ch = fis.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        byteBuffer.limit(4);
        int read = ch.read(byteBuffer);

        System.out.println(read);
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());

        System.out.println("----");

        byteBuffer.flip();
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());

        System.out.println("----");

        byteBuffer.get();

        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());

        System.out.println("----");

        byteBuffer.compact();
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());

    }
}
