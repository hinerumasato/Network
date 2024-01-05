package Teach.RMI.Expression;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerApp {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(1234);
        IAuthentication authentication = new AuthenticationImpl();
        registry.bind("auth", authentication);
    }
}
