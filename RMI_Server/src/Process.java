import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Process {
    private void startServer() throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(2003);
        IServer server = new ServerImpl();
        registry.rebind("server", server);
    }

    public static void main(String[] args) throws RemoteException {
        new Process().startServer();
    }
}
