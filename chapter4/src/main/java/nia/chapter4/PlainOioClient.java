package nia.chapter4;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Listing 4.1 Blocking networking without Netty
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class PlainOioClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 12346);
        System.out.println("done connected!");
        InputStream inputStream = socket.getInputStream();
        int c;
        while ((c = inputStream.read()) != -1) {
            System.out.print((char) c);
        }
    }

}
