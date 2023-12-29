package Teach.SocketJDBC;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientProcess implements Runnable {
    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;
    private boolean exit;

    public ClientProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
        this.exit = false;
    }

    private void findByAge(int age) throws IOException {
        // Giao tiếp với Server
        String commandName = "findByAge";
        netOut.writeUTF(commandName);
        netOut.flush();
        netOut.writeInt(age);
        netOut.flush();
        // Đợi server xử lý ...

        // Xử lý kết quả từ server
        // Đọc size trước
        int size = netIn.readInt();
        for (int i = 0; i < size; i++) {
            // Đọc student details
            int id = netIn.readInt();
            String name = netIn.readUTF();
            int _age = netIn.readInt();
            double score = netIn.readDouble();
            System.out.println("ID = " + id + " Name = " + name + " Age = " + _age + " Score = " + score);
        }
    }

    private void queryStudent() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your command: ");
        String command = reader.readLine();
        StringTokenizer token = new StringTokenizer(command, " ");
        String commandName = token.nextToken();
        String commandValue = token.nextToken();

        switch (commandName) {
            case "findByAge":
                int age = Integer.parseInt(commandValue);
                findByAge(age);
                break;
            case "findByName":
            case "findByScore":
            case "findByID":
            case "findAll":

            default:
                break;
        }
    }

    private void insert() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String commandName = "insert";
        netOut.writeUTF(commandName);
        netOut.flush();

        System.out.print("Enter your command: ");
        String commandInsert = reader.readLine();

        netOut.writeUTF(commandInsert);
        netOut.flush();

        // Đợi server response
        String response = netIn.readUTF();
        System.out.println(response);
    }

    private void update() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String commandName = "update";
        netOut.writeUTF(commandName);
        netOut.flush();

        System.out.print("Enter your command: ");
        String commandUpdate = reader.readLine();

        netOut.writeUTF(commandUpdate);
        netOut.flush();

        // Đợi server response
        String response = netIn.readUTF();
        System.out.println(response);
    }

    private void delete() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String commandName = "delete";
        netOut.writeUTF(commandName);
        netOut.flush();

        System.out.print("Enter your command: ");
        String commandDelete = reader.readLine();

        netOut.writeUTF(commandDelete);
        netOut.flush();

        // Đợi server response
        String response = netIn.readUTF();
        System.out.println(response);
    }

    private void exit() throws IOException {
        netOut.writeUTF("exit");
        netOut.flush();
        this.exit = true;
    }

    @Override
    public void run() {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (!exit) {
                System.out.println("Chon chuc nang: ");
                System.out.println("1. Tra cuu");
                System.out.println("2. Them sinh vien");
                System.out.println("3. Sua sinh vien");
                System.out.println("4. Xoa sinh vien");
                System.out.println("Nhap exit de thoat chuong trinh");
                String command = reader.readLine();

                switch (command) {
                    case "1":
                        queryStudent();
                        break;
                    case "2":
                        insert();
                        break;
                    case "3":
                        update();
                        break;
                    case "4":
                        delete();
                        break;
                    case "exit":
                        exit();
                        break;

                    default:
                        System.out.println("Command khong hop le");
                }
            }

            reader.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
