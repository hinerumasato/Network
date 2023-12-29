package Teach.FTP;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class FTPClient {
    public static final int MAIN_PORT = 2000;
    public static final int FILE_PORT = 2001;
    public static final String HOST = "localhost";

    private void startClient() throws UnknownHostException, IOException {
        Socket socket = new Socket(HOST, MAIN_PORT);
        MainClientProcess process = new MainClientProcess(socket);
        new Thread(process).start();
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        new FTPClient().startClient();
    }
}
