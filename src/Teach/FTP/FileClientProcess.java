package Teach.FTP;

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
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
        this.type = type;
    }

    // Upload file lên server
    private void upload() throws IOException {
        StringTokenizer tokenizer = new StringTokenizer(command, " ");
        tokenizer.nextToken();
        String sourceFile = tokenizer.nextToken();
        String destFile = tokenizer.nextToken();

        File file = new File(sourceFile);
        // Dùng FileInputstream và bufferedInputStream để đọc dữ liệu của file
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[1024];
        int offset;

        // Ghi thông tin muốn tạo file lên server
        // Tên file upload
        // Đường dẫn file upload
        netOut.writeUTF(file.getName());
        netOut.flush();
        netOut.writeUTF(destFile);
        netOut.flush();
        netOut.writeLong(file.length());
        netOut.flush();

        while((offset = bis.read(buffer)) != -1) {
            netOut.write(buffer, 0, offset);
            netOut.flush();
        }

        bis.close();
    }

    private void download() {

    }

    // Xử lý download hoặc upload trên cổng 2001
    @Override
    public void run() {
        try {
            switch (type) {
                case UPLOAD:
                    upload();
                    break;
                case DOWNLOAD:
                    download();
                    break;
                default:
                    break;
            }

            socket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
