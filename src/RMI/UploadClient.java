package RMI;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class UploadClient {
	public static void main(String[] args) throws NotBoundException, IOException {
		Registry reg = LocateRegistry.getRegistry(1022);
		IUpload server = (IUpload) reg.lookup("UPLOAD");
		
		String sf="source";
		String df="source-copy";
		
		InputStream in = new BufferedInputStream(new FileInputStream(sf));
		String sid = server.createDf(df);
		
		int br;
		byte[] data = new byte [1024000];
		while((br=in.read()) != -1) {
			server.writeToDf(sid,data,br);
		}
		in.close();
		server.closeDf(sid);
	}
}
