package Teach;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static int PORT = 1234;
    private ServerSocket serverSocket;

    private void startServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            ServerProcess process = new ServerProcess(socket);
            new Thread(process).start();
        }
    }
    public static void main(String[] args) throws IOException {
        new Server().startServer();
    }
}
