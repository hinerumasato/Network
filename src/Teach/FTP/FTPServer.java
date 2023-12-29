package Teach.FTP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FTPServer {
    public static final int MAIN_PORT = 2000;
    public static final int FILE_PORT = 2001;

    private ServerSocket mainServerSocket;
    private ServerSocket fileServerSocket;

    private void startServer() throws IOException {
        System.out.println("Server started");
        this.mainServerSocket = new ServerSocket(MAIN_PORT);
        this.fileServerSocket = new ServerSocket(FILE_PORT);

        while (true) {
            Socket socket = mainServerSocket.accept();
            System.out.println("Client " + socket.getInetAddress() + " connected");
            MainServerProcess process = new MainServerProcess(socket, fileServerSocket);
            new Thread(process).start();
        }
    }

    public static void main(String[] args) throws IOException {
        new FTPServer().startServer();
    }
}
