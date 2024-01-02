package Teach.RMI.Student;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface IServer extends Remote {
    public String login(String username, String password) throws RemoteException, ClassNotFoundException, SQLException;
    public String logout(String sessionId) throws RemoteException;
    public List<Student> findByAge(int age, String sessionId) throws RemoteException, ClassNotFoundException, SQLException;
}
