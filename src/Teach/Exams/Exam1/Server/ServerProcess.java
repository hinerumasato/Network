package Teach.Exams.Exam1.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerProcess implements Runnable {

    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;
    private boolean isExit;
    private Service service;

    public ServerProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
        this.isExit = false;
        this.service = Service.getInstance();
    }

    @Override
    public void run() {
        try {
            while (!isExit) {
                String command = netIn.readUTF();
                StringTokenizer tokenizer = new StringTokenizer(command, "\t");
                String commandName = tokenizer.nextToken();
                switch (commandName) {
                    case "REGISTER":
                        String name = tokenizer.nextToken();
                        String birthday = tokenizer.nextToken();
                        String address = tokenizer.nextToken();

                        Candidate candidate = new Candidate(name, birthday, address);
                        if(service.isValidCandidate(candidate)) {
                            service.insertCandidate(candidate);
                            netOut.writeUTF("OK");
                            netOut.flush();
                            netOut.writeInt(candidate.getId());
                            netOut.flush();
                        } else {
                            netOut.writeUTF("Tuoi khong hop le");
                            netOut.flush();
                        }
                        break;
                    case "FOTO":
                        long size = netIn.readLong();
                        if(size <= 100000) {
                            netOut.writeUTF("OK");
                            netOut.flush();
                            byte[] buffer = new byte[100000];
                            netIn.read(buffer);
                            int id = netIn.readInt();
                            service.writeFile(id, buffer);
                            netOut.writeUTF("OK");
                            netOut.flush();
                        } else {
                            netOut.writeUTF("FALSE");
                            netOut.flush();
                        }
                        break;
                    case "VIEW":
                        int vId = Integer.parseInt(tokenizer.nextToken());
                        if(service.isExist(vId)) {
                            netOut.writeUTF("OK");
                            netOut.flush();

                            netOut.writeUTF(service.getUserData(vId));
                            netOut.flush();
                        } else {
                            netOut.writeUTF("Khong ton tai thi sinh trong he thong");
                            netOut.flush();
                        }
                        break;
                    case "UPDATE":
                        int uId = Integer.parseInt(tokenizer.nextToken());
                        String uAddress = tokenizer.nextToken();
                        service.update(uId, uAddress);
                        
                        netOut.writeUTF("OK");
                        netOut.flush();
                        break;
                    case "QUIT":
                        System.out.println("Client " + socket.getInetAddress() + " has disconnected");
                        this.isExit = true;
                        break;
                
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
