package Exams.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static int PORT = 6969;

    private void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client " + socket.getInetAddress() + " has connnected");
            ServerProcess process = new ServerProcess(socket);
            new Thread(process).start();
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().startServer();
    }
}
