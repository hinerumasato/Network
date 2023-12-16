package FTP;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class FileServerProcess implements Runnable {

    private Socket socket;
    private EServerFile type;
    private DataInputStream netIn;
    private DataOutputStream netOut;

    public FileServerProcess(Socket socket, EServerFile type) throws IOException {
        this.socket = socket;
        this.type = type;
        netIn = new DataInputStream(socket.getInputStream());
        netOut = new DataOutputStream(socket.getOutputStream());
    }

    private void receiveFile() throws IOException {
        String destination = netIn.readUTF();
        String fileName = netIn.readUTF();
        long fileSize = netIn.readLong();
        
        File file = new File(destination + File.separator + fileName);
        file.createNewFile();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        int offset;
        long byteReaded = 0;
        byte[] buffer = new byte[1024];
        while(byteReaded < fileSize) {
            offset = netIn.read(buffer);
            bos.write(buffer, 0, offset);
            byteReaded += offset;
        }
        bos.close();
    }

    @Override
    public void run() {
        try {
            System.out.println("PORT: " + FTPServer.FILE_PORT + " has opened");
            switch (type) {
                case RECEIVE:
                    receiveFile();
                    break;
                case SEND:
                    break;
                default:
                    break;
            }
            System.out.println("PORT: " + FTPServer.FILE_PORT + " has closed");
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

    public EServerFile getType() {
        return type;
    }

    public void setType(EServerFile type) {
        this.type = type;
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
