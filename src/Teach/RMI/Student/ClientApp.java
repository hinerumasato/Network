package Teach.RMI.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.List;

public class ClientApp {
    public static void main(String[] args) throws NotBoundException, IOException, ClassNotFoundException, SQLException {
        Registry registry = LocateRegistry.getRegistry("localhost", 12345);
        IServer server = (IServer) registry.lookup("server");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String sessionId = "";
        while (true) {
            String request = reader.readLine();
            if(!request.startsWith("user ")) {
                if(sessionId == "")
                    System.out.println("Chua dang nhap, khong duoc truy van");
                else {
                    // findByAge
                    if(request.startsWith("findByAge ")) {
                        int age = Integer.parseInt(request.split(" ")[1]);
                        List<Student> students = server.findByAge(age, sessionId);
                        if(students != null) {
                            if(students.size() > 0)
                                System.out.println(students);
                            else System.out.println("Khong tim thay");
                        } else {
                            System.out.println("Session ID khong hop le");
                        }
                    } else if(request.startsWith("logout")) {
                        String response = server.logout(sessionId);
                        System.out.println(response);
                    }
                }
            } else {
                String username = request.split(" ")[1];
                String nextCommand = reader.readLine();
                if(nextCommand.startsWith("pass ")) {
                    String password = nextCommand.split(" ")[1];
                    sessionId = server.login(username, password);
                    if(!sessionId.equals(""))
                        System.out.println("Dang nhap thanh cong");
                    else System.out.println("Ten dang nhap hoac mat khau khong hop le");
                } else {
                    System.out.println("Command khong hop le");
                }
            }
        }
    }
}
