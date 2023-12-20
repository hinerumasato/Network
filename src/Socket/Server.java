package Socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2222);
        System.out.println("Listening ----");
        Socket socket = serverSocket.accept();
        DataOutputStream netOut = new DataOutputStream(socket.getOutputStream());
        netOut.writeUTF("Xin chào Client .... ");
        serverSocket.close();
    }
}
