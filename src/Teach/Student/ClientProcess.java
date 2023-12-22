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
                if(!isLogin) {
                    // Xử lý đăng nhập
                    System.out.print("user ");
                    String username = reader.readLine();
                    System.out.print("pass ");
                    String password = reader.readLine();

                    // Gửi lên cho server kiểm tra
                    netOut.writeUTF(username);
                    netOut.flush();
                    netOut.writeUTF(password);
                    netOut.flush();

                    // Sau khi gửi thì đọc dữ liệu đăng nhập từ server
                    String loginResponse = netIn.readUTF();
                    // Nếu nội dung response là "success"
                    if(loginResponse.equals("success")) {
                        isLogin = true;
                        System.out.println("Login Successfully");
                    }
                } else {
                    System.out.print("Enter your command: ");
                    String command = reader.readLine();
                    // Gửi command tra cứu lên server
                    netOut.writeUTF(command);
                    netOut.flush();

                    // Đợi phản từ server
                    List<Student> students = receiveServerResponse();
                    // In kết quả sau khi truy vấn
                    printStudents(students);
                    
                }
            }

            reader.close();
            socket.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private List<Student> receiveServerResponse() throws IOException {
        List<Student> result = new ArrayList<Student>();
        int size = netIn.readInt();
        for(int i = 0; i < size; i++) {
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
