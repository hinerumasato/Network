package Teach.RMI.RMIServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl extends UnicastRemoteObject implements IServer {

    protected ServerImpl() throws RemoteException {
        super();
    }

    @Override
    public void echo(String message) throws RemoteException {
        System.out.println(message);
    }
    
}
