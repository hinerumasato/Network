package Teach.Exams.Exam1.Client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientProcess {

    private static String COMMAND_FALSE = "Command khong hop le";

    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;
    private boolean isExit;
    private boolean isRegistered;
    private boolean isUploaded;
    private int id;

    public ClientProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
        this.isExit = false;
        this.isRegistered = false;
        this.isUploaded = false;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!isExit) {
                String command = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(command, "\t");
                int countTokens = tokenizer.countTokens();
                String commandName = tokenizer.nextToken();
                switch (commandName) {
                    case "REGISTER":
                        if (countTokens == 4) {
                            if (!isRegistered) {
                                netOut.writeUTF(command);
                                netOut.flush();

                                // Đợi response từ server
                                String response = netIn.readUTF();
                                System.out.println(response);

                                if (response.equals("OK")) {
                                    this.id = netIn.readInt();
                                    System.out.println(id);
                                    isRegistered = true;
                                }
                            } else {
                                System.out.println("Da dang ky roi");
                            }
                        } else
                            System.out.println(COMMAND_FALSE);
                        break;
                    case "FOTO":
                        if (countTokens == 2) {
                            if (isRegistered) {
                                if (!isUploaded) {
                                    netOut.writeUTF(command);
                                    netOut.flush();

                                    String path = tokenizer.nextToken();

                                    File file = new File(path);
                                    long size = file.length();
                                    netOut.writeLong(size);
                                    netOut.flush();
                                    String canUploadMessage = netIn.readUTF();
                                    if (canUploadMessage.equals("OK")) {
                                        BufferedInputStream bis = new BufferedInputStream(
                                                new FileInputStream(file));
                                        byte[] buffer = new byte[100000];
                                        bis.read(buffer);
                                        netOut.write(buffer);
                                        bis.close();

                                        netOut.writeInt(this.id);
                                        netOut.flush();

                                        String response = netIn.readUTF();
                                        System.out.println(response);
                                        if (response.equals("OK")) {
                                            isUploaded = true;
                                        }
                                    } else {
                                        System.out.println("Size is too large");
                                    }

                                } else {
                                    System.out.println("khong duoc upload lai");
                                }
                            } else {
                                System.out.println("Chua dang ky");
                            }
                        } else {
                            System.out.println(COMMAND_FALSE);
                        }
                        break;
                    case "VIEW":
                        if (countTokens == 2) {
                            netOut.writeUTF(command);
                            netOut.flush();

                            String response = netIn.readUTF();
                            System.out.println(response);
                            if (response.equals("OK")) {
                                // In ra thông tin từ server
                                String userData = netIn.readUTF();
                                System.out.println(userData);
                            }
                        } else {
                            System.out.println(COMMAND_FALSE);
                        }
                        break;
                    case "UPDATE":
                        if(countTokens == 3) {
                            netOut.writeUTF(command);
                            netOut.flush();

                            String response = netIn.readUTF();
                            System.out.println(response);
                            break;
                        } else {
                            System.out.println(COMMAND_FALSE);
                        }
                    case "QUIT":
                        netOut.writeUTF(command);
                        netOut.flush();
                        isExit = true;
                        break;

                    default:
                        break;
                }
            }

            reader.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
