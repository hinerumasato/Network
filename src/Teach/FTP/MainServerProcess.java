package Teach.FTP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServerProcess implements Runnable {

    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;
    private boolean exit;
    private ServerSocket fileServerSocket;

    public MainServerProcess(Socket socket, ServerSocket fileServerSocket) throws IOException {
        this.socket = socket;
        this.fileServerSocket = fileServerSocket;
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
    }

    private void sendGreetingToClient() throws IOException {
        netOut.writeUTF("Hello Client, this is your server");
        netOut.flush();
    }

    private void exit() {
        this.exit = true;
        System.out.println("Client " + socket.getInetAddress() + " has disconnected");
    }

    private void openUploadServerSocket() throws IOException {
        // Chấp nhận cho phép client được quyền kết nối đến cổng 2001
        netOut.writeUTF("accept");
        netOut.flush();
        
        Socket fileSocket = fileServerSocket.accept();
        System.out.println("Cong 2001 da duoc client " + fileSocket.getInetAddress() + " ket noi");
        // Khởi tạo một tiến trình thứ 3 để upload
        FileServerProcess fileProcess = new FileServerProcess(fileSocket, EServerFile.RECEIVE);
        new Thread(fileProcess).start();
    }

    @Override
    public void run() {
        try {
            while (!exit) {
                // Nhận được command từ client
                String command = netIn.readUTF();
                switch (command) {
                    case "greet":
                        sendGreetingToClient();
                        break;
                    case "exit":
                        exit();
                        break;
                    // Nếu command của phía client là open socket 2001
                    case "openUploadServerSocket":
                        openUploadServerSocket();
                        break;
                    default:
                        break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
