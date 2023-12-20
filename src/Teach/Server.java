package Teach;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2222);
        System.out.println("Listening...");
        while (true) {
            Socket socket = serverSocket.accept();
            ServerProcess serverProcess = new ServerProcess(socket);
            new Thread(serverProcess).start();
            
        }
    }
}
