package Teach.RMI.Student;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServerImpl extends UnicastRemoteObject implements IServer {

    private Map<String, User> sessionMap = new HashMap<String, User>();
    private StudentService studentService = new StudentService();
    private UserService userService = new UserService();

    protected ServerImpl() throws RemoteException {
        super();
    }

    @Override
    public List<Student> findByAge(int age, String sessionId) throws RemoteException, ClassNotFoundException, SQLException {
        if(sessionMap.containsKey(sessionId)) {
            List<Student> students = studentService.findByAge(age);
            return students;
        } else {
            return null;
        }
    }

    @Override
    public String login(String username, String password) throws RemoteException, ClassNotFoundException, SQLException {
        String sessionId = "";
        User loginUser = userService.login(username, password);
        if(loginUser != null) {
            sessionId = UUID.randomUUID().toString();
            sessionMap.put(sessionId, loginUser);
            return sessionId;
        } else {
            return "";
        }
    }

    @Override
    public String logout(String sessionId) throws RemoteException {
        if(sessionMap.containsKey(sessionId)) {
            sessionMap.remove(sessionId);
            return "Dang xuat thanh cong";
        } else {
            return "Session ID Khong hop le";
        }
    }

}
