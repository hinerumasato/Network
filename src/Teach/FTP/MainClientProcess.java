package Teach.FTP;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainClientProcess implements Runnable {

    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;
    private boolean exit;

    public MainClientProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
        this.exit = false;
    }

    private void printGreetingFromServer() throws IOException {
        netOut.writeUTF("greet");
        netOut.flush();

        String greeting = netIn.readUTF();
        System.out.println(greeting);
    }

    private void exit() throws IOException {
        netOut.writeUTF("exit");
        netOut.flush();
        this.exit = true;
    }

    private void sendOpenUploadSocket(String command) throws IOException {
        // Ghi command mở cổng cho server
        netOut.writeUTF("openUploadServerSocket");
        netOut.flush();

        String response = netIn.readUTF();
        // Nếu server cho phép kết nối
        if(response.equals("accept")) {
            // Kết nối đến cổng 2001
            Socket fileSocket = new Socket(FTPClient.HOST, FTPClient.FILE_PORT);
            // Khởi tạo một tiến trình thứ 3 để upload
            // Đưa enum UPLOAD vào constructor để cho cổng 2001 biết client sẽ upload
            FileClientProcess fileProcess = new FileClientProcess(fileSocket, command, EClientFile.UPLOAD);
            new Thread(fileProcess).start();
        } else {
            System.out.println("Server khong cho phep mo cong");
        }
    }

    @Override
    public void run() {
        try {
            while (!exit) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Enter your command: ");
                String command = reader.readLine();
                String commandName = command.split(" ")[0];
                switch (commandName) {
                    case "greet":
                        printGreetingFromServer();
                        break;
                    case "exit":
                        exit();
                        break;

                    // Khi người dùng nhập lệnh upload
                    case "upload":
                        // Thông báo cho server biết mình cần sử dụng cổng 20001
                        // Gọi đến hàm upload
                        sendOpenUploadSocket(command);
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
