package Teach;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientProcess implements Runnable {

    private Socket socket;
    private BufferedReader netIn;
    private PrintWriter netOut;
    private boolean exit;

    public ClientProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.netOut = new PrintWriter(socket.getOutputStream());
        exit = false;
    }

    @Override
    public void run() {
        try {
            Scanner reader = new Scanner(System.in);
            while (!exit) {
                System.out.print("Enter your command: ");
                String command = reader.nextLine();
                if(command.equalsIgnoreCase("exit")) {
                    netOut.println("exit");
                    netOut.flush();
                    exit = true;
                }
                else {
                    netOut.println(command);
                    netOut.flush();

                    String response = netIn.readLine();
                    System.out.println(response);
                }
            }

            reader.close();
            socket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
