package Teach.RMI.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Teach.RMI.RMIServer.IServer;

public class App {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 1234);
        IServer server = (IServer) registry.lookup("server");
        server.echo("Hello Server");
    }
}
