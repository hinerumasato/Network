package FTP;

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
        this.exit = false;
        netIn = new DataInputStream(socket.getInputStream());
        netOut = new DataOutputStream(socket.getOutputStream());
    }

    private void getGreeting() throws IOException {
        netOut.writeUTF("greeting");
        netOut.flush();

        String response = netIn.readUTF();
        System.out.println(response);
    }

    private void exit() throws IOException {
        netOut.writeUTF("exit");
        netOut.flush();
        this.exit = true;
    }

    private void openUploadSocket(String command) throws IOException {

        netOut.writeUTF("openUploadServerSocket");
        netOut.flush();

        String response = netIn.readUTF();

        if(response.equals("accept")) {
            Socket fileSocket = new Socket(FTPClient.HOST, FTPClient.FILE_PORT);
            FileClientProcess process = new FileClientProcess(fileSocket, command, EClientFile.UPLOAD);
            new Thread(process).start();
        } else {
            System.out.println("Server has refused your open upload socket command");
        }

    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!exit) {
                System.out.print("Enter your command: ");
                String command = reader.readLine();
                String commandName = command.split(" ")[0];
                switch (commandName) {
                    case "greeting":
                        getGreeting();
                        break;
                    case "exit":
                        exit();
                        break;
                    case "upload":
                        openUploadSocket(command);
                        break;
                    default:
                        break;
                }
            }
            reader.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }
}
