package Teach.RMI.Expression;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientApp {

    public static final String HOST = "localhost";
    public static final int PORT = 1234;
    public static void main(String[] args) throws NotBoundException, IOException {
        Registry registry = LocateRegistry.getRegistry(HOST, PORT);
        IAuthentication auth = (IAuthentication) registry.lookup("auth");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String username = "abc";
        String password = "abcwewer";
        String sessionId = auth.login(username, password);
        while (true) {
            // Đang sử dụng app
            String request = reader.readLine();
            // Yêu cầu lấy ra thông tin của user
            if(request.equals("get")) {
                User user = auth.getUserData(sessionId);
                if(user != null)
                    System.out.println(user);
                else System.out.println("sessionid khong hop le");
            }
        }
    }
}
