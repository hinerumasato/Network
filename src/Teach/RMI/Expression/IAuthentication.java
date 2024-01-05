package Teach.RMI.Expression;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAuthentication extends Remote {
    public String login(String username, String password) throws RemoteException;
    public User getUserData(String sessionId) throws RemoteException;
}
