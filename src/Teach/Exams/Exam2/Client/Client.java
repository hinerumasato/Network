package Teach.Exams.Exam2.Client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static String HOST = "localhost";
    public static int PORT = 6969;

    private void startClient() throws UnknownHostException, IOException {
        Socket socket = new Socket(HOST, PORT);
        ClientProcess process = new ClientProcess(socket);
        process.run();
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        new Client().startClient();
    }
}
