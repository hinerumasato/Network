package Teach;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerProcess implements Runnable {

    private Socket socket;

    public ServerProcess(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            DataInputStream reader = new DataInputStream(socket.getInputStream());
            writer.writeUTF("Hello client");
            writer.flush();
            writer.writeUTF("This is server");
            writer.flush();
            writer.writeInt(3);
            writer.flush();
            System.out.println(reader.readUTF());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
