package Socket.Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerProcess implements Runnable {

    private Socket socket;
    private BufferedReader netIn;
    private PrintWriter netOut;

    public ServerProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.netOut = new PrintWriter(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            netOut.println("Hello Hello");
            netOut.flush();
            while (true) {
                System.out.println(netIn.readLine());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getNetIn() {
        return netIn;
    }

    public void setNetIn(BufferedReader netIn) {
        this.netIn = netIn;
    }

    public PrintWriter getNetOut() {
        return netOut;
    }

    public void setNetOut(PrintWriter netOut) {
        this.netOut = netOut;
    }

}
