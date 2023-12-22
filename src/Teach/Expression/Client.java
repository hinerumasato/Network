package Teach.Expression;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static int PORT = 1234;
    public static String HOST = "localhost";

    private void startClient() throws UnknownHostException, IOException {
        Socket socket = new Socket(HOST, PORT);
        ClientProcess process = new ClientProcess(socket);
        new Thread(process).start();
    }

    public static void main(String[] args) throws IOException {
        new Client().startClient();
    }
}
