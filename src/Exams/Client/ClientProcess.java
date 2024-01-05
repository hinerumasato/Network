package Exams.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientProcess implements Runnable {
    private Socket socket;
    private BufferedReader netIn;
    private PrintWriter netOut;
    private boolean isLogin;
    private boolean isExit;

    public ClientProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.netOut = new PrintWriter(socket.getOutputStream(), true);
        isLogin = false;
        isExit = false;
    }

    @Override
    public void run() {
        try {
            String welcome = netIn.readLine();
            System.out.println(welcome);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!isExit) {
                if (!isLogin) {
                    String userCommand = reader.readLine();
                    if (userCommand.startsWith("user\t")) {
                        netOut.println(userCommand);
                        String userResponse = netIn.readLine();
                        System.out.println(userResponse);
                        if (userResponse.equalsIgnoreCase("OK")) {
                            String passwordCommand = reader.readLine();
                            if (passwordCommand.startsWith("pass\t")) {
                                netOut.println(passwordCommand);
                                String passwordResponse = netIn.readLine();
                                System.out.println(passwordResponse);
                                if (passwordResponse.equalsIgnoreCase("OK"))
                                    isLogin = true;
                            } else {
                                System.out.println("Command is invalid");
                            }
                        }
                    } else if (userCommand.startsWith("QUIT")) {
                        netOut.println("quit");
                        isExit = true;
                    } else {
                        System.out.println("Command is invalid");
                    }
                } else {
                    String command = reader.readLine();
                    StringTokenizer tokenizer = new StringTokenizer(command, "\t");
                    int countToken = tokenizer.countTokens();
                    String commandName = tokenizer.nextToken();
                    switch (commandName) {
                        case "ADD":
                            if (countToken == 5) {
                                netOut.println(command);
                                String response = netIn.readLine();
                                System.out.println(response);
                            } else {
                                System.out.println("Command is invalid");
                            }
                            break;
                        case "REMOVE":
                            if (countToken > 0) {
                                netOut.println(command);
                                String response = netIn.readLine();
                                System.out.println(response);
                            } else {
                                System.out.println("Command is invalid");
                            }
                            break;
                        case "EDIT":
                            if (countToken == 5) {
                                netOut.println(command);
                                String response = netIn.readLine();
                                System.out.println(response);
                            } else {
                                System.out.println("Command is invalid");
                            }
                            break;
                        case "VIEW":
                            if (countToken > 0) {
                                netOut.println(command);
                                int size = Integer.parseInt(netIn.readLine());
                                System.out.println(size);
                                for (int i = 0; i < size; i++) {
                                    String line = netIn.readLine();
                                    System.out.println(line);
                                }
                            } else {
                                System.out.println("Command is invalid");
                            }
                            break;
                        case "QUIT":
                            netOut.println(command);
                            isLogin = false;
                            break;
                        default:
                            System.out.println("Command is invalid");
                            break;
                    }
                }
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
