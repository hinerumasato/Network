package Teach.Exams.Exam2.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientProcess {
    private Socket socket;
    private BufferedReader netIn;
    private PrintWriter netOut;
    private boolean isLogin;
    private boolean isExit;

    public ClientProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.netOut = new PrintWriter(socket.getOutputStream());
        this.isLogin = false;
        this.isExit = false;
    }

    public void run() {
        try {
            String welcome = netIn.readLine();
            System.out.println(welcome);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!isExit) {
                if(!isLogin) {
                    String userCommand = reader.readLine();
                    StringTokenizer userTokenizer = new StringTokenizer(userCommand, "\t");
                    String userCommandName = userTokenizer.nextToken();
                    if(userCommandName.startsWith("USER")) {

                        netOut.println(userCommand);
                        netOut.flush();
                        String response = netIn.readLine();
                        System.out.println(response);
                        if(response.equals("OK")) {
                            String passwordCommand = reader.readLine();
                            StringTokenizer passwordTokenizer = new StringTokenizer(passwordCommand, "\t");
                            String passwordCommandName = passwordTokenizer.nextToken();
                            if(passwordCommandName.startsWith("PASS")) {
                                netOut.println(passwordCommand);
                                netOut.flush();

                                String loginResponse = netIn.readLine();
                                System.out.println(loginResponse);
                                if(loginResponse.equals("OK")) {
                                    isLogin = true;
                                }
                            } else {
                                System.out.println("Command khong hop le");
                            }
                        }
                    } 
                    else if (userCommand.equals("EXIT")) {
                        netOut.println("EXIT");
                        netOut.flush();
                        isExit = true;
                    }
                    else {
                        System.out.println("Command khong hop le");
                    }
                } else {
                    String command = reader.readLine();
                    StringTokenizer tokenizer = new StringTokenizer(command, "\t");
                    int countTokens = tokenizer.countTokens();
                    String commandName = tokenizer.nextToken();
                    switch (commandName) {
                        case "ADD":
                            if(countTokens == 5) {
                                netOut.println(command);
                                netOut.flush();

                                String response = netIn.readLine();
                                System.out.println(response);
                            } else {
                                System.out.println("Command khong hop le");
                            }
                            break;
                        case "REMOVE":
                            if(countTokens > 1) {
                                netOut.println(command);
                                netOut.flush();

                                String response = netIn.readLine();
                                System.out.println(response);
                            } else {
                                System.out.println("Command khong hop le");
                            }
                            break;
                        case "EDIT":
                            if(countTokens == 5) {
                                netOut.println(command);
                                netOut.flush();

                                String response = netIn.readLine();
                                System.out.println(response);
                            } else {
                                System.out.println("Command khong hop le");
                            }
                            break;
                        case "VIEW":
                            if(countTokens == 2) {
                                netOut.println(command);
                                netOut.flush();

                                int length = Integer.parseInt(netIn.readLine());
                                for(int i = 0; i < length; i++) {
                                    String line = netIn.readLine();
                                    System.out.println(line);
                                }
                            } else {
                                System.out.println("Command khong hop le");
                            }
                            break;
                        case "QUIT":
                            if(countTokens == 1) {
                                netOut.println(command);
                                netOut.flush();
                                isLogin = false;
                            } else {
                                System.out.println("Command khong hop le");
                            }
                            break;
                        default:
                            break;
                    }

                }
            }

            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
