package FTP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServerProcess implements Runnable {

    private ServerSocket fileServer;
    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;

    private boolean exit;

    public MainServerProcess(ServerSocket fileServer, Socket socket) throws IOException {
        this.fileServer = fileServer;
        this.socket = socket;
        this.exit = false;
        netIn = new DataInputStream(socket.getInputStream());
        netOut = new DataOutputStream(socket.getOutputStream());
    }

    private void sendGreeting() throws IOException {
        netOut.writeUTF("Hello, this is a server from address: 127.0.0.1");
        netOut.flush();
    }

    private void exit() {
        this.exit = true;
    }

    private void openUploadServerSocket(String command) throws IOException {
        netOut.writeUTF("accept");
        netOut.flush();

        Socket fileSocket = fileServer.accept();
        FileServerProcess process = new FileServerProcess(fileSocket, EServerFile.RECEIVE);
        new Thread(process).start();
    }

    @Override
    public void run() {
        try {
            while (!exit) {
                String request = netIn.readUTF();
                switch (request) {
                    case "greeting":
                        sendGreeting();
                        break;
                    case "exit":
                        exit();
                    case "openUploadServerSocket":
                        openUploadServerSocket(request);
                        break;
                    default:
                        break;
                }
                if(exit) break;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getFileServer() {
        return fileServer;
    }

    public void setFileServer(ServerSocket fileServer) {
        this.fileServer = fileServer;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getNetIn() {
        return netIn;
    }

    public void setNetIn(DataInputStream netIn) {
        this.netIn = netIn;
    }

    public DataOutputStream getNetOut() {
        return netOut;
    }

    public void setNetOut(DataOutputStream netOut) {
        this.netOut = netOut;
    }
}
