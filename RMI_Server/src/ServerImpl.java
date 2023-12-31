import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl extends UnicastRemoteObject implements IServer {

    protected ServerImpl() throws RemoteException {
        super();
    }

    @Override
    public void echoClientMessage(String message) {
        System.out.println("Client said: " + message);
    }
    
}
