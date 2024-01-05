package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2222);
        System.out.println("Listening ----");
        while (true) {
            Socket socket = serverSocket.accept();
            PrintWriter netOut = new PrintWriter(socket.getOutputStream());
            BufferedReader netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            netOut.println("Xin chào Client .... ");
            netOut.flush();

            String response = netIn.readLine();
            System.out.println(response);
        }
    }
}
