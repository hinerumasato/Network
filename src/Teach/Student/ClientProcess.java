package Teach.Student;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientProcess implements Runnable {
    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;
    private boolean exit;
    private boolean isLogin;

    public ClientProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
        this.exit = false;
        this.isLogin = false;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!exit) {
                System.out.print("Enter your command: ");
                String command = reader.readLine();
                if (!command.startsWith("user ")) {
                    if (!isLogin) {
                        System.out.println("Can dang nhap de thuc hien thao tac nay");
                    } else {
                        // Gửi command tra cứu lên server
                        netOut.writeUTF(command);
                        netOut.flush();

                        // Đợi phản từ server
                        List<Student> students = receiveServerResponse();
                        // In kết quả sau khi truy vấn
                        printStudents(students);
                    }
                } else {
                    if (!isLogin) {
                        // Xử lý đăng nhập
                        String username = command.split(" ")[1];
                        System.out.print("Password command: ");
                        String password = reader.readLine();
                        if(password.startsWith("pass ")) {
                            // Gửi lên cho server kiểm tra
                            password = password.split(" ")[1];
                            netOut.writeUTF(username);
                            netOut.flush();
                            netOut.writeUTF(password);
                            netOut.flush();
                        } else {
                            System.out.println("Lenh dang nhap khong hop le");
                        }

                        // Sau khi gửi thì đọc dữ liệu đăng nhập từ server
                        String loginResponse = netIn.readUTF();
                        // Nếu nội dung response là "success"
                        if (loginResponse.equals("success")) {
                            isLogin = true;
                            System.out.println("Login Successfully");
                        }
                    }
                }
            }

            reader.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Student> receiveServerResponse() throws IOException {
        List<Student> result = new ArrayList<Student>();
        int size = netIn.readInt();
        for (int i = 0; i < size; i++) {
            String name = netIn.readUTF();
            int age = netIn.readInt();
            double score = netIn.readDouble();
            Student student = new Student(name, age, score);
            result.add(student);
        }
        return result;
    }

    private void printStudents(List<Student> students) {
        for (Student student : students) {
            System.out.println(student);
        }
    }

}
