package Teach;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket("localhost", 2222);

        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        DataInputStream reader = new DataInputStream(is);
        DataOutputStream writer = new DataOutputStream(os);

        String message = reader.readUTF();
        String message2 = reader.readUTF();
        int data = reader.readInt();
        writer.writeUTF("Client has been received");
        writer.flush();

        System.out.println(message);
        System.out.println(message2);
        System.out.println(data);
    }
}
