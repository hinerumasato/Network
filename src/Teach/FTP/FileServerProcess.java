package Teach.FTP;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class FileServerProcess implements Runnable {

    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;
    private EServerFile type;

    public FileServerProcess(Socket socket, EServerFile type) throws IOException {
        this.socket = socket;
        this.type = type;
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
    }

    private void receiveFile() throws IOException {
        String fileName = netIn.readUTF();
        String destFolder = netIn.readUTF();
        long fileSize = netIn.readLong();

        File file = new File(destFolder + File.separator + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

        byte[] buffer = new byte[1024];
        int byteRead = 0;
        int offset;
        while(byteRead < fileSize) {
            offset = netIn.read(buffer);
            bos.write(buffer, 0, offset);
            byteRead += offset;
        }

        bos.close();
    }

    @Override
    public void run() {
        try {
            switch (type) {
                case RECEIVE:
                    receiveFile();
                    break;
            
                default:
                    break;
            }

            System.out.println("Client has disconnected from port 2001");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
