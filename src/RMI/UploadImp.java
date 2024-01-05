package RMI;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadImp extends UnicastRemoteObject implements IUpload {
	private Map<String, OutputStream> session;

	protected UploadImp() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		session = new HashMap<String, OutputStream>();
	}

	/**
	 * 
	 */

	@Override
	public String createDf(String df) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			String sid = UUID.randomUUID().toString();
			OutputStream out = new BufferedOutputStream(new FileOutputStream(df));
			session.put(sid, out);
			return sid;

		} catch (Exception e) {
			// TODO: handle exception
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public void writeToDf(String sessionId, byte[] data, int size) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			OutputStream out = session.get(sessionId);
			if(out == null) {
				throw new RemoteException("invalid");
			}
			out.write(data, 0, size);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public void closeDf(String sessionId) throws RemoteException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		try {
			OutputStream out = session.get(sessionId);
			if(out == null) {
				throw new RemoteException("invalid");
			}
			out.close();
			session.remove(sessionId);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RemoteException(e.getMessage());
		}
	}

}
