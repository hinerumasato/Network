package Teach.RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl extends UnicastRemoteObject implements IServer {

    protected ServerImpl() throws RemoteException {
        super();
    }

    @Override
    public void echo(String message) throws RemoteException {
        // In message từ phía Client gửi lên Server
        System.out.println(message);
    }
    
}
