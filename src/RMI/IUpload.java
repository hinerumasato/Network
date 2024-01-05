package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUpload extends Remote {
	String createDf(String df) throws RemoteException;
	void writeToDf(String sessionid, byte[] data, int size)throws RemoteException;
	void closeDf(String sessionid) throws RemoteException;
}
