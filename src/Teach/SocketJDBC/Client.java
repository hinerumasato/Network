package Teach.SocketJDBC;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static String HOST = "192.168.1.11";
    public static int PORT = 1234;

    private void startClient() throws UnknownHostException, IOException {
        Socket socket = new Socket(HOST, PORT);
        ClientProcess process = new ClientProcess(socket);
        new Thread(process).start();
    }
    public static void main(String[] args) throws UnknownHostException, IOException {
        new Client().startClient();
    }
}
