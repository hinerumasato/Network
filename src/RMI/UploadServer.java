package RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class UploadServer {
public static void main(String[] args) throws RemoteException {
	Registry reg = LocateRegistry.createRegistry(1022);
	UploadImp upload = new UploadImp();
	reg.rebind("UPLOAD", upload);
}
}
