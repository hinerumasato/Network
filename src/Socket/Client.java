package Socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 2222);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String message = dis.readUTF();
        System.out.println(message);
        socket.close();
    }
}
