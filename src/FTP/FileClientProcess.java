package FTP;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class FileClientProcess implements Runnable {

    private Socket socket;
    private String command;
    private DataInputStream netIn;
    private DataOutputStream netOut;
    private EClientFile type;

    public FileClientProcess(Socket socket, String command, EClientFile type) throws IOException {
        this.socket = socket;
        this.command = command;
        this.type = type;
        netIn = new DataInputStream(socket.getInputStream());
        netOut = new DataOutputStream(socket.getOutputStream());
    }

    private void upload() throws IOException {
        StringTokenizer tokenizer = new StringTokenizer(command);
        tokenizer.nextToken();
        String source = tokenizer.nextToken();
        String destination = tokenizer.nextToken();
        netOut.writeUTF(destination);

        File file = new File(source);

        netOut.writeUTF(file.getName());
        netOut.flush();

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[1024];
        int offset;

        netOut.writeLong(file.length());
        netOut.flush();

        while((offset = bis.read(buffer)) != -1) {
            netOut.write(buffer, 0, offset);
            netOut.flush();
        }        
        bis.close();
        socket.close();
    }

    @Override
    public void run() {
        try {
            switch (type) {
                case UPLOAD:
                    upload();
                    break;
                case DOWNLOAD:
                    break;
    
                default:
                    break;
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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
