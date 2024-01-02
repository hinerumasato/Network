package Teach.RMI.RMIServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {
    public void echo(String message) throws RemoteException;
}
